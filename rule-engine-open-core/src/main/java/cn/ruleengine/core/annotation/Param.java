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
 * 只适用在普通数据类型上，例如String，Integer，Boolean，BigDecimal，List，Set
 * <p>
 * executor(@Param(value = "pattern",required = false) String pattern,@Param(value = "timeZone",required = false) String timeZone)
 * 建议加上@Param
 *
 * @author dingqianwen
 * @date 2020/8/4
 * @since 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 方法参数别名
     *
     * @return String
     */
    String value() default "";

    /**
     * 默认如果参数不存在，抛出ValidException异常
     *
     * @return boolean
     */
    boolean required() default true;

}
