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

import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 〈WorkspaceInterceptor〉
 *
 * @author 丁乾文
 * @date 2023/8/27 15:28
 * @since 1.0.0
 */
@Slf4j
@Component
public class WorkspaceInterceptor extends HandlerInterceptorAdapter {


    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        UserData currentUser = Context.getCurrentUser();
        if (currentUser == null) {
            return true;
        }
        // 设置当前工作空间，以及判断是否有此工作空间权限
        RBucket<Workspace> bucket = this.redissonClient.getBucket(WorkspaceService.CURRENT_WORKSPACE + currentUser.getId());
        Workspace workspace = bucket.get();
        if (workspace == null) {
            // 权限被移除了，需要重新登录，设置一个工作空间
            throw new ApiException(ErrorCodeEnum.RULE_4012);
        } else {
            log.info("当前工作空间：" + workspace);
        }
        Context.setCurrentWorkspace(workspace);
        return true;
    }

}
