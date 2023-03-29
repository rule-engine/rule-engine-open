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
package cn.ruleengine.core;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface Input {

    /**
     * 限制，外部规则入参不能以$_开头，系统内部使用，传递一些参数
     */
    String SYSTEM_KEY_PREFIX = "$_";

    /**
     * 添加一个输入参数
     *
     * @param key   参数key
     * @param value 参数值
     */
    void put(String key, Object value);

    /**
     * 添加多个输入参数
     *
     * @param inputParam 参数
     */
    void putAll(Map<String, Object> inputParam);

    /**
     * 移除参数名称对应的参数值
     *
     * @param key 参数名称
     * @return 返回参数名称对应的参数值
     */
    Object remove(String key);

    /**
     * 获取参数对象
     *
     * @param key 参数名称
     * @return 获取参数名称对应的参数值
     */
    Object get(String key);

    /**
     * 获取所有的参数
     *
     * @return map
     */
    Map<String, Object> getAll();

    /**
     * 清除所有参数
     */
    void clear();

}
