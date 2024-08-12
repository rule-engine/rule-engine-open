package cn.ruleengine.web.interceptor;

import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.web.annotation.Auth;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.util.ResponseUtils;
import cn.ruleengine.web.vo.user.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 〈AuthInterceptor〉
 *
 * @author 丁乾文
 * @date 2023/8/27 15:23
 * @since 1.0.0
 */
@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {


    /**
     * 校验带有Auth注解的方法是否有权限
     *
     * @param request  r
     * @param response r
     * @param handler  h
     * @return true
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        UserData currentUser = Context.getCurrentUser();
        // 如果无需登录的，此处也跳过
        if (currentUser == null) {
            return true;
        }
        // 如果存在需要验证权限,并且权限没有验证通过时,提示无权限访问
        if (!this.hasRoleAuth(handler, currentUser)) {
            // Token验证通过,但是用户无权限访问
            log.warn("无权限访问,User:{}", currentUser);
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE99990403.getCode(), ErrorCodeEnum.RULE99990403.getMsg()));
            return false;
        }
        return true;
    }


    /**
     * 是否需要校验角色权限
     *
     * @param handler  h
     * @param userData u
     * @return r
     */
    private boolean hasRoleAuth(Object handler, UserData userData) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        boolean methodRoleAuth = method.isAnnotationPresent(Auth.class);
        if (!methodRoleAuth) {
            return true;
        }
        // 如果是超级管理
        if (userData.getIsAdmin()) {
            return true;
        }
        Auth auth = method.getAnnotation(Auth.class);
        Auth.Role role = auth.accessibleRole();
        // 如果需要工作空间管理权限
        if (role == Auth.Role.WORKSPACE_ADMINISTRATOR) {
            return Context.getCurrentWorkspace().isAdministration();
        }
        return false;
    }

}
