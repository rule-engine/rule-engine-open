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
package cn.ruleengine.web.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    /**
     * 可访问角色
     * <p>
     * 权限向下兼容，SUPER_ADMINISTRATOR总是最高权限
     *
     * @return role
     */
    Role accessibleRole() default Role.SUPER_ADMINISTRATOR;

    enum Role {

        /**
         * 超级管理
         */
        SUPER_ADMINISTRATOR,
        /**
         * 工作空间管理
         */
        WORKSPACE_ADMINISTRATOR,

    }

}
