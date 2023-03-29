/**
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
package cn.ruleengine.web.annotation;

import cn.ruleengine.web.enums.RateLimitEnum;
import org.redisson.api.RateIntervalUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 * 〈接口限流〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 每个周期内请求次数,默认60秒内一个这个ip地址只能请求一次此接口
     *
     * @return int
     */
    long limit() default 1L;

    /**
     * 周期时间内触发
     *
     * @return int
     */
    long refreshInterval() default 60L;

    /**
     * 限流类型,默认根据ip限制
     *
     * @return RateLimitEnum
     */
    RateLimitEnum type() default RateLimitEnum.IP;

    /**
     * 时间单位
     *
     * @return RateIntervalUnit
     */
    RateIntervalUnit rateIntervalUnit() default RateIntervalUnit.SECONDS;
}
