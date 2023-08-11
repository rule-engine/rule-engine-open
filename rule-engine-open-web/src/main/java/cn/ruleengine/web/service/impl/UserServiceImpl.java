package cn.ruleengine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.HtmlTemplatesEnum;
import cn.ruleengine.web.enums.UserType;
import cn.ruleengine.web.enums.VerifyCodeType;
import cn.ruleengine.web.exception.LoginException;
import cn.ruleengine.web.interceptor.AuthInterceptor;
import cn.ruleengine.web.service.UserService;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.store.entity.RuleEngineUser;
import cn.ruleengine.web.store.entity.RuleEngineUserWorkspace;
import cn.ruleengine.web.store.manager.RuleEngineUserManager;
import cn.ruleengine.web.store.manager.RuleEngineUserWorkspaceManager;
import cn.ruleengine.web.util.*;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.user.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author liqian
 * @date 2020/9/24
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RuleEngineUserManager ruleEngineUserManager;
    @Resource
    private RuleEngineUserWorkspaceManager ruleEngineUserWorkspaceManager;
    @Resource
    private RedissonClient redissonClient;
    @Autowired(required = false)
    private EmailClient emailClient;
    @Resource
    private AliOSSClient aliOssClient;

    @Value("${auth.redis.token.keyPrefix:token:}")
    public String tokenKeyPrefix;
    @Value("${auth.redis.token.keepTime:3600000}")
    public Long redisTokenKeepTime;
    @Value("${auth.jwt.issuer:ruleengine}")
    private String issuer;

    /**
     * 注册时验证码存入redis的前缀
     */
    private static final String REGISTER_EMAIL_CODE_PRE = "rule_engine_user_register_email_code_pre:";
    /**
     * 忘记密码时验证码存入redis的前缀
     */
    private static final String FORGOT_EMAIL_CODE_PRE = "rule_engine_boot_user_forgot_email_code_pre:";

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @return true表示登录成功
     */
    @Override
    public boolean login(LoginRequest loginRequest) {
        RuleEngineUser ruleEngineUser = ruleEngineUserManager.lambdaQuery()
                .and(a -> a.eq(RuleEngineUser::getUsername, loginRequest.getUsername())
                        .or().eq(RuleEngineUser::getEmail, loginRequest.getUsername())).one();
        if (ruleEngineUser == null) {
            throw new LoginException("用户名/邮箱不存在!");
        }
        if (!(ruleEngineUser.getPassword().equals(MD5Utils.encrypt(loginRequest.getPassword())))) {
            throw new LoginException("登录密码错误!");
        }
        String token = JWTUtils.genderToken(String.valueOf(ruleEngineUser.getId()), this.issuer, ruleEngineUser.getUsername());
        HttpServletResponse response = HttpServletUtils.getResponse();
        response.setHeader(HttpServletUtils.ACCESS_CONTROL_EXPOSE_HEADERS, AuthInterceptor.TOKEN);
        response.setHeader(AuthInterceptor.TOKEN, token);
        this.refreshUserData(token, ruleEngineUser);
        return true;
    }

    /**
     * 刷新存在redis中的用户数据
     *
     * @param token          token
     * @param ruleEngineUser 用户信息
     */
    private void refreshUserData(String token, RuleEngineUser ruleEngineUser) {
        UserData userData = BasicConversion.INSTANCE.convert(ruleEngineUser);
        RBucket<UserData> bucket = this.redissonClient.getBucket(this.tokenKeyPrefix.concat(token));
        //保存到redis,用户访问时获取
        bucket.set(userData, this.redisTokenKeepTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 用户注册
     *
     * @param registerRequest 注册信息
     * @return true表示注册成功
     */
    @Override
    public Boolean register(RegisterRequest registerRequest) {
        checkVerifyCode(registerRequest.getEmail(), registerRequest.getCode(), REGISTER_EMAIL_CODE_PRE);
        if (verifyName(registerRequest.getUsername())) {
            throw new ValidationException("用户名已经存在!");
        }
        if (verifyEmail(registerRequest.getEmail())) {
            throw new ValidationException("邮箱已经存在!");
        }
        RuleEngineUser ruleEngineUser = new RuleEngineUser();
        ruleEngineUser.setUsername(registerRequest.getUsername());
        ruleEngineUser.setPassword(MD5Utils.encrypt(registerRequest.getPassword()));
        ruleEngineUser.setEmail(registerRequest.getEmail());
        ruleEngineUserManager.save(ruleEngineUser);
        return true;
    }

    /**
     * 验证用户名是否重复
     *
     * @param username username
     * @return Boolean
     */
    @Override
    public Boolean verifyName(String username) {
        return null != ruleEngineUserManager.lambdaQuery()
                .eq(RuleEngineUser::getUsername, username)
                .one();
    }

    /**
     * 忘记密码获取验证码
     *
     * @param verifyCodeByEmailRequest 邮箱/类型:注册,忘记密码
     * @return BaseResult
     */
    @Override
    public Boolean verifyCodeByEmail(GetVerifyCodeByEmailRequest verifyCodeByEmailRequest) {
        Integer type = verifyCodeByEmailRequest.getType();
        String email = verifyCodeByEmailRequest.getEmail();
        if (VerifyCodeType.FORGOT.getValue().equals(type)) {
            //忘记密码时检查此邮箱在本系统中是否存在
            if (!verifyEmail(email)) {
                throw new ValidationException("你输入的邮箱账号在本系统中不存在");
            }
            //获取验证码时,把当前邮箱获取的验证码存入到redis,备用
            verifyCodeProcess(FORGOT_EMAIL_CODE_PRE, email);
        } else if (VerifyCodeType.REGISTER.getValue().equals(type)) {
            //注册获取验证码,获取验证码时,把当前邮箱获取的验证码存入到redis,备用
            verifyCodeProcess(REGISTER_EMAIL_CODE_PRE, email);
        } else {
            throw new ValidationException("不支持的类型");
        }
        return true;
    }

    /**
     * 验证邮箱是否重复
     *
     * @param email email
     * @return Boolean
     */
    @Override
    public Boolean verifyEmail(String email) {
        return null != ruleEngineUserManager.lambdaQuery()
                .eq(RuleEngineUser::getEmail, email)
                .one();
    }

    /**
     * 发送验证码消息
     *
     * @param pre   pre
     * @param email 邮箱
     */
    private void verifyCodeProcess(String pre, String email) {
        RBucket<Integer> rBucket = redissonClient.getBucket(pre + IPUtils.getRequestIp() + email);
        //生成验证码
        int randomCode = (int) ((Math.random() * 9 + 1) * 10000);
        //设置有效期10分钟
        rBucket.set(randomCode, 600, TimeUnit.SECONDS);
        Map<Object, Object> params = new HashMap<>(1);
        params.put("code", randomCode);
        //发送验证码邮件
        emailClient.sendSimpleMail(params, HtmlTemplatesEnum.EMAIL.getMsg(), HtmlTemplatesEnum.EMAIL.getValue(), email);
    }

    /**
     * 修改密码
     *
     * @param forgotRequest forgotRequest
     * @return Boolean
     */
    @Override
    public Boolean updatePassword(ForgotRequest forgotRequest) {
        checkVerifyCode(forgotRequest.getEmail(), forgotRequest.getCode(), FORGOT_EMAIL_CODE_PRE);
        RuleEngineUser ruleEngineUser = new RuleEngineUser();
        ruleEngineUser.setPassword(MD5Utils.encrypt(forgotRequest.getPassword()));
        return ruleEngineUserManager.lambdaUpdate()
                .eq(RuleEngineUser::getEmail, forgotRequest.getEmail())
                .update(ruleEngineUser);
    }

    /**
     * 获取登录人信息
     *
     * @return user
     */
    @Override
    public UserResponse getUserInfo() {
        UserData userData = Context.getCurrentUser();
        return BasicConversion.INSTANCE.convert(userData);
    }


    /**
     * 上传用户头像
     * BasicConversion
     *
     * @param file 图片文件
     * @return 图片url
     */
    @Override
    public String uploadAvatar(MultipartFile file) throws IOException {
        return this.aliOssClient.upload(file.getInputStream(), file.getOriginalFilename());
    }

    /**
     * 更新用户信息
     *
     * @param userInfoRequest 根据id更新用户信息
     * @return 用户信息
     */
    @Override
    public Boolean updateUserInfo(UpdateUserInfoRequest userInfoRequest) {
        UserData currentUser = Context.getCurrentUser();
        // 如果不是管理员 或者不是修改的自己的用户信息，都是没权限的
        if (!currentUser.getIsAdmin() && !currentUser.getId().equals(userInfoRequest.getId())) {
            throw new ValidationException("无权限修改!");
        }
        RuleEngineUser ruleEngineUser = this.ruleEngineUserManager.getById(userInfoRequest.getId());
        if (ruleEngineUser == null) {
            throw new ValidationException("没有此用户!");
        }
        ruleEngineUser.setId(userInfoRequest.getId());
        ruleEngineUser.setEmail(userInfoRequest.getEmail());
        ruleEngineUser.setPhone(userInfoRequest.getPhone());
        ruleEngineUser.setAvatar(userInfoRequest.getAvatar());
        ruleEngineUser.setSex(userInfoRequest.getSex());
        ruleEngineUser.setDescription(userInfoRequest.getDescription());
        this.ruleEngineUserManager.updateById(ruleEngineUser);
        // 如果当前登陆人是自己，更新当前登陆用户信息
        if (currentUser.getId().equals(userInfoRequest.getId())) {
            String token = HttpServletUtils.getRequest().getHeader(AuthInterceptor.TOKEN);
            this.refreshUserData(token, ruleEngineUser);
        }
        return true;
    }

    /**
     * 用户列表
     *
     * @param pageRequest p
     * @return r
     */
    @Override
    public PageResult<ListUserResponse> list(PageRequest<ListUserRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        ListUserRequest query = pageRequest.getQuery();
        return PageUtils.page(ruleEngineUserManager, pageRequest.getPage(), () -> {
            QueryWrapper<RuleEngineUser> wrapper = new QueryWrapper<>();
            LambdaQueryWrapper<RuleEngineUser> lambda = wrapper.lambda();
            if (Validator.isNotEmpty(query.getUsername())) {
                lambda.like(RuleEngineUser::getUsername, query.getUsername());
            }
            if (Validator.isNotEmpty(query.getSex())) {
                lambda.eq(RuleEngineUser::getSex, query.getSex());
            }
            if (Validator.isNotEmpty(query.getEmail())) {
                lambda.like(RuleEngineUser::getEmail, query.getEmail());
            }
            PageUtils.defaultOrder(orders, wrapper);
            return wrapper;
        }, m -> {
            ListUserResponse listUserResponse = new ListUserResponse();
            BeanUtil.copyProperties(m, listUserResponse);
            return listUserResponse;
        });
    }

    /**
     * 添加用户
     *
     * @param addUserRequest a
     * @return r
     */
    @Override
    public Boolean add(AddUserRequest addUserRequest) {
        RuleEngineUser ruleEngineUser = new RuleEngineUser();
        BeanUtil.copyProperties(addUserRequest, ruleEngineUser);
        // md5
        ruleEngineUser.setPassword(MD5Utils.encrypt(addUserRequest.getPassword()));
        ruleEngineUser.setIsAdmin(UserType.GENERAL_USER.getType());
        this.ruleEngineUserManager.save(ruleEngineUser);
        // 默认工作空间
        RuleEngineUserWorkspace ruleEngineUserWorkspace = new RuleEngineUserWorkspace();
        ruleEngineUserWorkspace.setUserId(ruleEngineUser.getId());
        ruleEngineUserWorkspace.setWorkspaceId(WorkspaceService.DEFAULT_WORKSPACE_ID);
        ruleEngineUserWorkspace.setIsAdministration(UserType.GENERAL_USER.getType());
        this.ruleEngineUserWorkspaceManager.save(ruleEngineUserWorkspace);
        return true;
    }

    /**
     * 删除用户
     *
     * @param deleteUserRequest a
     * @return r
     */
    @Override
    public Boolean delete(DeleteUserRequest deleteUserRequest) {
        Integer id = deleteUserRequest.getId();
        // 删除用户信息表数据
        this.ruleEngineUserManager.lambdaUpdate()
                .eq(RuleEngineUser::getId, id)
                .remove();
        // 删除所有空间内该用户信息
        this.ruleEngineUserWorkspaceManager.lambdaUpdate()
                .eq(RuleEngineUserWorkspace::getUserId, id)
                .remove();
        return true;
    }

    /**
     * 通过id获取用户信息
     *
     * @param selectUserRequest s
     * @return user
     */
    @Override
    public SelectUserResponse selectUserById(SelectUserRequest selectUserRequest) {
        RuleEngineUser ruleEngineUser = this.ruleEngineUserManager.getById(selectUserRequest.getId());
        if (ruleEngineUser == null) {
            throw new ValidationException("没有此用户!");
        }
        SelectUserResponse selectUserResponse = new SelectUserResponse();
        BeanUtils.copyProperties(ruleEngineUser, selectUserResponse);
        return selectUserResponse;
    }

    /**
     * 检查验证码是否有效
     *
     * @param email 邮箱
     * @param code  验证码
     * @param pre   redis key pre
     */
    private void checkVerifyCode(String email, Integer code, String pre) {
        String userEmailCodePre = pre + IPUtils.getRequestIp() + email;
        RBucket<Integer> rBucket = redissonClient.getBucket(userEmailCodePre);
        Integer getCode = rBucket.get();
        if (getCode == null) {
            throw new ValidationException("验证码已失效!");
        }
        if (!getCode.equals(code)) {
            throw new ValidationException("验证码错误!");
        }
    }

    /**
     * 用户id与用户信息的映射
     *
     * @param userIds 用户id
     * @return map
     */
    @Override
    public Map<Integer, RuleEngineUser> getMapByUserIds(Set<Integer> userIds) {
        List<RuleEngineUser> ruleEngineUsers = ruleEngineUserManager.lambdaQuery().in(RuleEngineUser::getId, userIds).list();
        return ruleEngineUsers.stream().collect(Collectors.toMap(RuleEngineUser::getId, Function.identity()));
    }

}
