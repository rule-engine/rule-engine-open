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
package cn.ruleengine.core.listener;

import cn.ruleengine.core.DataSupport;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * <p>
 * 监听器业务逻辑将影响引擎处理性能，业务尽可能使用异步管道操作
 *
 * @author dingqianwen
 * @date 2020/8/3
 * @since 1.0.0
 */
public interface ExecuteListener<T extends DataSupport> {

    Logger log = LoggerFactory.getLogger(ExecuteListener.class);

    /**
     * 执行之前
     *
     * @param t     执行的规则
     * @param input 输入参数
     */
    default void before(T t, Input input) {

    }

    /**
     * 执行发生异常时
     *
     * @param t         执行的规则
     * @param input     输入参数
     * @param exception 异常信息
     */
    default void onException(T t, Input input, Exception exception) {

    }

    /**
     * 执行之后
     *
     * @param t      执行的规则
     * @param input  输入参数
     * @param output 规则执行结果
     */
    default void after(T t, Input input, Output output) {

    }

}
