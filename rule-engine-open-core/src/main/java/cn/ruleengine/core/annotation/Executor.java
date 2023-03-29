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
package cn.ruleengine.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 标记为函数主方法
 *
 * @author dingqianwen
 * @date 2020/7/19
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Executor {

    /**
     * 失败最大尝试次数,目前主要适用在发送邮件/或者短信的函数失败后重新尝试执行
     * <p>
     * 如果重试全部失败，则寻找FailureStrategy执行
     *
     * @return 默认不重试
     */
    int maxAttempts() default 0;

    /**
     * 重试时间间隔，毫秒
     *
     * @return long
     */
    long delay() default 0;

    /**
     * 遇到这些异常时，触发函数失败策略方法
     * 默认只要存在带FailureStrategy注解的方法，遇到任何异常失败则会执行
     *
     * @return class
     */
    Class<? extends Throwable>[] failureFor() default {Throwable.class};

    /**
     * 排除这些异常触发函数失败策略方法
     *
     * @return class
     */
    Class<? extends Throwable>[] noFailureFor() default {};

}
