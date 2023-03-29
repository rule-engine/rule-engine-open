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


import cn.hutool.core.collection.CollUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public class DefaultInput implements Input {

    /**
     * 项目中使用到的运行参数
     */
    private final Map<String, Object> inputParam;

    public DefaultInput() {
        this.inputParam = new HashMap<>();
    }

    public DefaultInput(Map<String, Object> inputParam) {
        this.inputParam = Objects.requireNonNull(inputParam);
    }

    /**
     * 添加一个输入参数
     *
     * @param key   参数key
     * @param value 参数值
     */
    @Override
    public void put(String key, Object value) {
        this.inputParam.put(key, value);
    }

    /**
     * 添加多个输入参数
     *
     * @param inputParam 参数
     */
    @Override
    public void putAll(Map<String, Object> inputParam) {
        if (CollUtil.isEmpty(inputParam)) {
            return;
        }
        this.inputParam.putAll(inputParam);
    }

    /**
     * 移除参数名称对应的参数值
     *
     * @param key 参数名称
     * @return 返回参数名称对应的参数值
     */
    @Override
    public Object remove(String key) {
        return this.inputParam.remove(key);
    }

    /**
     * 获取参数对象
     *
     * @param key 参数名称
     * @return 获取参数名称对应的参数值
     */
    @Override
    public Object get(String key) {
        return this.inputParam.get(key);
    }


    /**
     * 获取所有的参数
     *
     * @return map
     */
    @Override
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(this.inputParam);
    }

    /**
     * 清除所有参数
     */
    @Override
    public void clear() {
        this.inputParam.clear();
    }

}
