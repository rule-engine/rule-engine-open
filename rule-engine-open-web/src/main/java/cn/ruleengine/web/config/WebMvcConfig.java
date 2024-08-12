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
package cn.ruleengine.web.config;


import cn.ruleengine.web.interceptor.AuthInterceptor;
import cn.ruleengine.web.interceptor.TraceInterceptor;
import cn.ruleengine.web.interceptor.TokenInterceptor;
import cn.ruleengine.web.interceptor.WorkspaceInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


/**
 * 〈一句话功能简述〉<br>
 * 〈mvc Interceptor〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private TraceInterceptor traceInterceptor;
    @Resource
    private TokenInterceptor tokenInterceptor;
    @Resource
    private WorkspaceInterceptor workspaceInterceptor;
    @Resource
    private AuthInterceptor authInterceptor;

    /**
     * 静态资源不拦截
     */
    private static final List<String> STATIC_RESOURCE = Arrays.asList(
            // swagger
            "/swagger-ui.html/**", "/swagger-resources/**", "/webjars/**", "/v2/**", "/csrf/**", "/doc.html/**",
            // druid
            "/druid/**",
            "/favicon.ico/**",
            "/error/**");

    /**
     * @param registry 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.traceInterceptor).addPathPatterns("/**")
                .excludePathPatterns(STATIC_RESOURCE);
        // 需保证顺序
        registry.addInterceptor(this.tokenInterceptor).addPathPatterns("/**")
                .excludePathPatterns(STATIC_RESOURCE).order(10);
        registry.addInterceptor(this.workspaceInterceptor).addPathPatterns("/**")
                .excludePathPatterns(STATIC_RESOURCE).order(11);
        registry.addInterceptor(this.authInterceptor).addPathPatterns("/**")
                .excludePathPatterns(STATIC_RESOURCE).order(12);
    }

}
