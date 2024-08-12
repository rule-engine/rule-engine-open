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
package cn.ruleengine.web.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.web.annotation.NoLogin;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.util.JWTUtils;
import cn.ruleengine.web.util.ResponseUtils;
import cn.ruleengine.web.vo.user.UserData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * 〈一句话功能简述〉<br>
 * 〈Token认证
 * 如果不需要token认证,则在controller的方法上加上NoAuth注解〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Slf4j
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    public static final String TOKEN = "token";

    @Value("${auth.redis.token.keyPrefix:token:}")
    public String tokenKeyPrefix;
    @Value("${auth.redis.token.keepTime:3600000}")
    public Long tokenKeepTime;
    /**
     * 是否开启鉴权
     */
    @Value("${auth.enable:true}")
    private boolean enable;

    @Resource
    private RedissonClient redissonClient;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        //如果配置文件中开启了验证权限
        if (!this.enable) {
            return true;
        }
        //如果Controller有NoAuth注解的方法,不需要验证权限
        if (isHaveAccess(handler)) {
            log.debug("此{}接口不需要认证权限", request.getRequestURI());
            return true;
        }
        //获取Header中的token
        String token = request.getHeader(TOKEN);
        if (StrUtil.isNullOrUndefined(token)) {
            log.warn("Token为空");
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE10010004.getCode(), ErrorCodeEnum.RULE10010004.getMsg()));
            return false;
        }
        try {
            // 对token进行验证
            JWTUtils.verifyToken(token);
        } catch (Exception e) {
            log.warn("Token验证不通过,Token:{}", token);
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE10011039.getCode(), ErrorCodeEnum.RULE10011039.getMsg()));
            return false;
        }
        // 从redis获取到用户信息保存到本地
        RBucket<UserData> bucket = this.redissonClient.getBucket(this.tokenKeyPrefix.concat(token));
        // 获取redis中存的用户信息
        UserData userData = bucket.get();
        if (userData == null) {
            log.warn("验证信息失效!");
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE99990402.getCode(), ErrorCodeEnum.RULE99990402.getMsg()));
            return false;
        }
        Context.setCurrentUser(userData);
        //更新过期时间
        bucket.expire(this.tokenKeepTime, TimeUnit.MILLISECONDS);
        log.debug("Token验证通过,User:{}", userData);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception exception) {
    }

    /**
     * 是否需要验证权限
     *
     * @param handler handler
     * @return 返回true时不鉴权
     */
    private boolean isHaveAccess(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        NoLogin responseBody = AnnotationUtils.findAnnotation(method, NoLogin.class);
        return responseBody != null;
    }


}


