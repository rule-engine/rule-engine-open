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

import cn.ruleengine.core.cache.DefaultFunctionCache;
import cn.ruleengine.core.cache.FunctionCache;
import cn.ruleengine.core.listener.DefaultExecuteListener;
import cn.ruleengine.core.listener.ExecuteListener;
import cn.ruleengine.core.rule.GeneralRule;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Objects;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/14
 * @since 1.0.0
 */
@Getter
public class RuleEngineConfiguration extends Container implements Closeable {

    /**
     * 规则执行监听器,可以动态的在规则调用之前或之后对一些规则进行特殊处理
     */
    private ExecuteListener<GeneralRule> generalRuleListener = new DefaultExecuteListener<>();


    /**
     * 规则函数缓存实现类
     */
    private FunctionCache functionCache = new DefaultFunctionCache();

    /**
     * 规则引擎变量
     */
    private EngineVariable engineVariable = new EngineVariable();

    /**
     * 设置普通规则执行监听器
     *
     * @param generalRuleListener 普通规则执行监听器
     */
    public void setGeneralRuleListener(@NonNull ExecuteListener<GeneralRule> generalRuleListener) {
        Objects.requireNonNull(generalRuleListener);
        this.generalRuleListener = generalRuleListener;
    }

    /**
     * 设置函数缓存实现类
     *
     * @param functionCache 函数缓存实现类
     */
    public void setFunctionCache(@NonNull FunctionCache functionCache) {
        Objects.requireNonNull(functionCache);
        this.functionCache = functionCache;
    }

    /**
     * 设置规则引擎变量
     *
     * @param engineVariable 规则引擎变量
     */
    public void setEngineVariable(@NonNull EngineVariable engineVariable) {
        Objects.requireNonNull(engineVariable);
        this.engineVariable = engineVariable;
    }

    @Override
    public void close() {
        super.close();
        this.engineVariable.close();
        this.functionCache.clear();
    }

}
