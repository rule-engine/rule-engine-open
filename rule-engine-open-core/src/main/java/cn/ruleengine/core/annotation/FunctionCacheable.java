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
package cn.ruleengine.core.annotation;

import cn.ruleengine.core.cache.DefaultKeyGenerator;
import cn.ruleengine.core.cache.KeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/14
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionCacheable {

    /**
     * 是否启用函数缓存
     *
     * @return 如果为true，则启用
     */
    boolean enable() default true;

    /**
     * 缓存的生存时间，单位：ms
     *
     * @return long
     */
    long liveOutTime() default 10 * 10000L;

    /**
     * 缓存的key生成策略
     *
     * @return class
     */
    Class<? extends KeyGenerator> keyGenerator() default DefaultKeyGenerator.class;

}
