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


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Objects;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 新年好！！
 *
 * @author dingqianwen
 * @date 2020/2/29
 * @since 1.0.0
 */
@Slf4j
public abstract class Engine implements Closeable {


    /**
     * 规则引擎运行所需的参数
     */
    @Getter
    private final RuleEngineConfiguration configuration;

    /**
     * 可传入配置信息，包括规则监听器，规则变量...
     *
     * @param configuration 规则引擎运行所需配置参数
     */
    public Engine(@NonNull RuleEngineConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    /**
     * 根据入参来执行引擎，并返回结果
     *
     * @param input         输入参数
     * @param workspaceCode 工作空间code
     * @param code          规则/决策表Code
     * @return 规则引擎计算的结果
     */
    public abstract Output execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String code);

    /**
     * 销毁规则引擎
     */
    @Override
    public void close() {
    }

}
