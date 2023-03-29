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

import cn.ruleengine.core.exception.ValueException;
import cn.ruleengine.core.value.Value;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/10
 * @since 1.0.0
 */
public class EngineVariable implements Closeable {

    /**
     * 规则引擎运行中的所使用的变量/函数
     * <p>
     * key存放的是变量id，value是变量对象
     * <p>
     * 因为变量是程序中可以动态变化的值，当规则引用的变量值发生了变化时，只需要发送mq消息，把此变量值替换了即可
     */
    private final Map<Integer, Value> variableMap = new ConcurrentHashMap<>();

    /**
     * 从引擎中删除一个变量
     *
     * @param id 变量id
     */
    public void removeVariable(Integer id) {
        this.variableMap.remove(id);
    }

    /**
     * 根据id从规则引擎中获取一个变量
     *
     * @param id 变量名称
     * @return 变量值
     */
    public Value getVariable(@NonNull Integer id) {
        Objects.requireNonNull(id);
        if (!this.variableMap.containsKey(id)) {
            throw new ValueException("No such variable：" + id);
        }
        return variableMap.get(id);
    }

    /**
     * 往规则引擎中添加一个变量
     *
     * @param id    变量id
     * @param value 变量值
     */
    public void addVariable(Integer id, Value value) {
        if (id == null || value == null) {
            throw new ValueException("Variable id or value cannot be Null");
        }
        this.variableMap.put(id, value);
    }

    /**
     * 往规则引擎中添加多个变量/函数
     *
     * @param variableMap 多个变量信息
     */
    public void addMultipleVariable(@NonNull Map<Integer, Value> variableMap) {
        variableMap.forEach(this::addVariable);
    }

    /**
     * 变量size
     *
     * @return size
     */
    public int size() {
        return this.variableMap.size();
    }

    /**
     * 清除规则引擎中所有的变量信息
     */
    @Override
    public void close() {
        this.variableMap.clear();
    }

}
