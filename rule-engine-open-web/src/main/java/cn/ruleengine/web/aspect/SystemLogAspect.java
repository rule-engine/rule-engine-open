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

import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.ruleengine.web.annotation.SystemLog;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.interceptor.TraceInterceptor;
import cn.ruleengine.web.listener.event.SystemLogEvent;
import cn.ruleengine.web.store.entity.RuleEngineSystemLog;
import cn.ruleengine.web.util.HttpServletUtils;
import cn.ruleengine.web.util.IPUtils;
import cn.ruleengine.web.vo.user.UserData;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈如果带有SystemLog注解,持久化操作日志〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Component
@Aspect
public class SystemLogAspect {

    @Resource
    private ApplicationEventPublisher eventPublisher;


    @Around("@annotation(systemLog)")
    private Object before(ProceedingJoinPoint joinPoint, SystemLog systemLog) throws Throwable {
        HttpServletRequest request = HttpServletUtils.getRequest();
        String agent = request.getHeader(Header.USER_AGENT.toString());
        UserAgent userAgent = UserAgentUtil.parse(agent);
        //设置日志参数
        RuleEngineSystemLog log = new RuleEngineSystemLog();
        //日志说明
        log.setDescription(systemLog.description());
        //请求开始时间
        log.setCreateTime(new Date());
        log.setTag(systemLog.tag());
        //请求用户id and username
        UserData userData = Context.getCurrentUser();
        if (userData != null) {
            log.setUserId(userData.getId());
            log.setUsername(userData.getUsername());
        }
        //请求ip地址
        log.setIp(IPUtils.getRequestIp());
        //浏览器
        log.setBrowser(userAgent.getBrowser().toString());
        //浏览器版本
        log.setBrowserVersion(userAgent.getVersion());
        //系统
        log.setSystem(userAgent.getOs().toString());
        //详情
        log.setDetailed(agent);
        //是否为移动平台
        log.setMobile(userAgent.isMobile());
        //请求参数
        log.setAges(JSON.toJSONString(joinPoint.getArgs()));
        //请求url
        log.setRequestUrl(request.getRequestURL().toString());
        // 过滤掉requestId:
        log.setRequestId(TraceInterceptor.getRequestId());
        try {
            //执行被代理类方法
            Object proceed = joinPoint.proceed();
            log.setReturnValue(JSON.toJSONString(proceed));
            return proceed;
        } catch (Throwable throwable) {
            //与是否触发了异常
            log.setException(throwable.getMessage());
            throw throwable;
        } finally {
            //请求结束时间
            Date endTime = new Date();
            log.setEndTime(endTime);
            //运行时间,就是执行用了多久执行完毕
            Long runningTime = endTime.getTime() - log.getCreateTime().getTime();
            log.setRunningTime(runningTime);
            log.setUpdateTime(endTime);
            //对日志持久化,日志使用@Async异步在高并发情况下仍然会出现问题,这里使用消息队列
            this.eventPublisher.publishEvent(new SystemLogEvent(log));
        }
    }
}
