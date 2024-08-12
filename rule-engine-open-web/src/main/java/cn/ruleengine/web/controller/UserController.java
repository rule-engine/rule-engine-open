/*
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.annotation.*;
import cn.ruleengine.web.enums.RateLimitEnum;
import cn.ruleengine.web.service.UserService;
import cn.ruleengine.web.vo.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/31
 * @since 1.0.0
 */
@Api(tags = "用户控制器")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param registerRequest 注册信息
     * @return true表示注册成功
     */
    @ReSubmitLock
    @NoLogin
    @PostMapping("register")
    @ApiOperation("用户注册")
    public PlainResult<Boolean> register(@RequestBody @Valid RegisterRequest registerRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(userService.register(registerRequest));
        return plainResult;
    }
    /**
     * 验证用户名字是否可用
     *
     * @param verifyUserNameRequest 用户名
     * @return true表示可以用
     */
    @PostMapping("verifyUserName")
    @ApiOperation("检查用户名字是否可用")
    public PlainResult<Boolean> checkUserName( @RequestBody @Valid VerifyUserNameRequest verifyUserNameRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(!userService.verifyName(verifyUserNameRequest.getUsername()));
        return plainResult;
    }
    /**
     * 验证用户邮箱是否可用
     *
     * @param verifyUserEmailRequest 用户邮箱
     * @return true表示可以用
     */
    @PostMapping("verifyEmail")
    @ApiOperation("检查用户邮箱是否可用")
    public PlainResult<Boolean> checkEmail( @RequestBody @Valid VerifyUserEmailRequest verifyUserEmailRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(!userService.verifyEmail(verifyUserEmailRequest.getEmail()));
        return plainResult;
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @return true表示登录成功
     */
    @NoLogin
    @SystemLog(tag = "用户登录")
    @RateLimit(limit = 6)
    @PostMapping("login")
    @ApiOperation("用户登录")
    public PlainResult<Boolean> login(@Valid @RequestBody LoginRequest loginRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(userService.login(loginRequest));
        return plainResult;
    }

    /**
     * 获取登录人信息
     *
     * @return user
     */
    @PostMapping("getUserInfo")
    @ApiOperation("获取登录人信息")
    public PlainResult<UserResponse> getUserInfo() {
        PlainResult<UserResponse> plainResult = new PlainResult<>();
        plainResult.setData(userService.getUserInfo());
        return plainResult;
    }

    /**
     * 根据邮箱获取验证码
     *
     * @param verifyCodeByEmailRequest 邮箱/类型:注册,忘记密码
     * @return BaseResult
     */
    @NoLogin
    @RateLimit(type = RateLimitEnum.URL_IP)
    @PostMapping("verifyCodeByEmail")
    @ApiOperation("根据邮箱获取验证码!")
    public BaseResult verifyCodeByEmail(@Valid @RequestBody GetVerifyCodeByEmailRequest verifyCodeByEmailRequest) {
        val result = new PlainResult<>();
        result.setData(userService.verifyCodeByEmail(verifyCodeByEmailRequest));
        return result;
    }

    /**
     * 上传用户头像
     *
     * @param file 图片文件
     * @return 图片url
     */
    @PostMapping("uploadAvatar")
    @ApiOperation("上传用户头像")
    public BaseResult uploadAvatar(MultipartFile file) throws IOException {
        val result = new PlainResult<>();
        result.setData(userService.uploadAvatar(file));
        return result;
    }

    /**
     * 更新用户信息
     *
     * @param updateUserInfoRequest 根据id更新用户信息
     * @return 用户信息
     */
    @ApiOperation("更新用户信息")
    @PostMapping("/updateUserInfo")
    public BaseResult updateUserInfo(@RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest) {
        val result = new PlainResult<>();
        result.setData(userService.updateUserInfo(updateUserInfoRequest));
        return result;
    }

    /**
     * 用户列表
     *
     * @param pageRequest p
     * @return r
     */
    @PostMapping("list")
    @ApiOperation("用户列表")
    public BaseResult list(@RequestBody PageRequest<ListUserRequest> pageRequest) {
        return this.userService.list(pageRequest);
    }

    /**
     * 添加用户
     *
     * @param addUserRequest a
     * @return r
     */
    @Auth
    @PostMapping("add")
    @ApiOperation("添加用户")
    public PlainResult<Boolean> add(@RequestBody @Valid AddUserRequest addUserRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(userService.add(addUserRequest));
        return plainResult;
    }

    /**
     * 删除用户
     *
     * @param deleteUserRequest a
     * @return r
     */
    @PostMapping("delete")
    @Auth()
    @ApiOperation("删除用户-(删除用户表信息，删除所有空间内该用户信息)")
    public PlainResult<Boolean> delete(@RequestBody @Valid DeleteUserRequest deleteUserRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(userService.delete(deleteUserRequest));
        return plainResult;
    }


    /**
     * 通过id获取用户信息
     *
     * @param selectUserRequest s
     * @return user
     */
    @PostMapping("selectUserById")
    @ApiOperation("通过id获取用户信息")
    public PlainResult<SelectUserResponse> selectUserById(@RequestBody @Valid SelectUserRequest selectUserRequest) {
        PlainResult<SelectUserResponse> plainResult = new PlainResult<>();
        plainResult.setData(userService.selectUserById(selectUserRequest));
        return plainResult;
    }

}
