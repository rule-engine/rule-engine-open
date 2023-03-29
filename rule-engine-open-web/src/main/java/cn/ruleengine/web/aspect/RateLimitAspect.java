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
package cn.ruleengine.web.aspect;


import cn.ruleengine.web.annotation.RateLimit;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.util.HttpServletUtils;
import cn.ruleengine.web.vo.user.UserData;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

/**
 * 〈一句话功能简述〉<br>
 * 〈接口级别限流,依赖于redis〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Component
@Aspect
@Slf4j
@Order(-8)
public class RateLimitAspect {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流key前缀,防止与其他redis key重复
     */
    private static final String KEY_PRE = "boot_engine:rate_limit_redis_key_pre:";

    /**
     * 存在bug，待优化
     *
     * @param joinPoint joinPoint
     * @param rateLimit rateLimit
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = KEY_PRE;
        switch (rateLimit.type()) {
            case IP:
                key += HttpServletUtils.getRequest().getRemoteAddr();
                break;
            case URL:
                key += HttpServletUtils.getRequest().getRequestURI();
                break;
            case USER:
                UserData userData = Context.getCurrentUser();
                if (userData == null) {
                    throw new RuntimeException("选择根据用户限流,但是并没有获取到用户登录信息!");
                }
                key += userData.getId().toString();
                break;
            case URL_IP:
                HttpServletRequest request = HttpServletUtils.getRequest();
                key += request.getRequestURI() + request.getRemoteAddr();
                break;
            default:
                throw new UnsupportedOperationException();
        }
        log.info("执行限流拦截器,限制类型:{},key:{}", rateLimit.type(), key);
        this.executor(key, rateLimit);
        return joinPoint.proceed();
    }

    /**
     * 限流执行器
     *
     * @param key       redis key
     * @param rateLimit 速率参数
     */
    private void executor(String key, RateLimit rateLimit) {
        //限制时间间隔
        long refreshInterval = rateLimit.refreshInterval();
        //限制时间间隔内可用次数
        long limit = rateLimit.limit();
        //时间单位
        RateIntervalUnit rateIntervalUnit = rateLimit.rateIntervalUnit();
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        //初始化RateLimiter的状态，并将配置存储到Redis服务器
        if (!rateLimiter.isExists()) {
            boolean trySetRate = rateLimiter.trySetRate(RateType.OVERALL, limit, refreshInterval, rateIntervalUnit);
            log.info("初始化RateLimiter的状态:{}", trySetRate);
        }
        if (!rateLimiter.tryAcquire()) {
            throw new ValidationException("你访问过于频繁,请稍后重试!");
        }
    }
}
