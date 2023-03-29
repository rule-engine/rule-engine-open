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
package cn.ruleengine.compute.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Slf4j
@Component
public class MDCLogInterceptor extends HandlerInterceptorAdapter {

    private final static String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 如果上游系统传入requestId则使用上游系统的requestId
        String requestId = request.getHeader(REQUEST_ID);
        if (StringUtils.isEmpty(requestId)) {
            // 否则生成一个
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(REQUEST_ID, REQUEST_ID.concat(StringPool.COLON).concat(requestId));
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception exception) {
        MDC.clear();
    }

    /**
     * 获取requestid
     *
     * @return requestid
     * @see MDCLogInterceptor#getRequestId(boolean)
     */
    public static String getRequestId() {
        return getRequestId(true);
    }

    /**
     * 获取requestid
     * <p>
     * requestId:b9557a4c-c86b-4f90-8782-8a24674ad3a9
     * false返回requestId:b9557a4c-c86b-4f90-8782-8a24674ad3a9
     * true返回b9557a4c-c86b-4f90-8782-8a24674ad3a9
     *
     * @param removePrefix 是否删除前缀requestId
     * @return requestid
     */
    public static String getRequestId(boolean removePrefix) {
        return Optional.ofNullable(MDC.get(MDCLogInterceptor.REQUEST_ID))
                .map(m -> removePrefix ? m.substring(10) : m)
                .orElse(null);
    }

}
