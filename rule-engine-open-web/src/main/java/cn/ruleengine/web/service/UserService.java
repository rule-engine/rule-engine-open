package cn.ruleengine.web.service;

import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.web.store.entity.RuleEngineUser;
import cn.ruleengine.web.vo.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author liqian
 * @date 2020/9/24
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @return true表示登录成功
     */
    boolean login(LoginRequest loginRequest);

    /**
     * 用户注册
     *
     * @param registerRequest 注册信息
     * @return true表示注册成功
     */
    Boolean register(RegisterRequest registerRequest);

    /**
     * 验证用户名是否重复
     *
     * @param username username
     * @return Boolean
     */
    Boolean verifyName(String username);

    /**
     * 忘记密码获取验证码
     *
     * @param verifyCodeByEmailRequest 邮箱/类型:注册,忘记密码
     * @return BaseResult
     */
    Object verifyCodeByEmail(GetVerifyCodeByEmailRequest verifyCodeByEmailRequest);

    /**
     * 验证邮箱是否重复
     *
     * @param email email
     * @return Boolean
     */
    Boolean verifyEmail(String email);

    /**
     * 修改密码
     *
     * @param forgotRequest forgotRequest
     * @return Boolean
     */
    Boolean updatePassword(ForgotRequest forgotRequest);

    /**
     * 获取登录人信息
     *
     * @return user
     */
    UserResponse getUserInfo();

    /**
     * 上传用户头像
     *
     * @param file 图片文件
     * @return 图片url
     */
    Object uploadAvatar(MultipartFile file) throws IOException;

    /**
     * 更新用户信息
     *
     * @param updateUserInfoRequest 根据id更新用户信息
     * @return 用户信息
     */
    Boolean updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest);

    /**
     * 用户列表
     *
     * @param pageRequest p
     * @return r
     */
    BaseResult list(PageRequest<ListUserRequest> pageRequest);

    /**
     * 添加用户
     *
     * @param addUserRequest a
     * @return r
     */
    Boolean add(AddUserRequest addUserRequest);

    /**
     * 删除用户
     *
     * @param deleteUserRequest a
     * @return r
     */
    Boolean delete(DeleteUserRequest deleteUserRequest);

    /**
     * 通过id获取用户信息
     *
     * @param selectUserRequest s
     * @return user
     */
    SelectUserResponse selectUserById(SelectUserRequest selectUserRequest);


    /**
     * 用户id与用户信息的映射
     *
     * @param userIds 用户id
     * @return map
     */
    Map<Integer, RuleEngineUser> getMapByUserIds(Set<Integer> userIds);
}
