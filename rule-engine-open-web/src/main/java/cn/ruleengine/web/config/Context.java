package cn.ruleengine.web.config;

import cn.hutool.core.util.ArrayUtil;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/22
 * @since 1.0.0
 */
@Slf4j
@Component
public class Context implements ApplicationContextAware {

    public static final String LOCAL_ENV = "local";
    public static final String DEV_ENV = "dev";
    public static final String TEST_ENV = "test";
    public static final String PRD_ENV = "prd";

    /**
     * 本次请求的工作空间信息
     */
    private static final ThreadLocal<Workspace> WORKSPACE = new ThreadLocal<>();
    /**
     * 本次请求的用户信息
     */
    private static final ThreadLocal<UserData> USER = new ThreadLocal<>();


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        Context.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return Context.applicationContext;
    }

    /**
     * 获取当前登录用户
     *
     * @return UserData
     */
    public static UserData getCurrentUser() {
        return Context.USER.get();
    }

    public static void setCurrentUser(UserData userData) {
        Context.USER.set(userData);
    }

    /**
     * 获取当前用户用户所在工作空间
     *
     * @return Workspace
     */
    public static Workspace getCurrentWorkspace() {
        return Context.WORKSPACE.get();
    }

    public static void setCurrentWorkspace(Workspace workspace) {
        Context.WORKSPACE.set(workspace);
    }


    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        return Context.applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile() {
        final String[] activeProfiles = Context.getActiveProfiles();
        return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
    }

    /**
     * 清楚所有ThreadLocal上下文数据
     */
    public static void clearAll() {
        WORKSPACE.remove();
        USER.remove();
    }

}
