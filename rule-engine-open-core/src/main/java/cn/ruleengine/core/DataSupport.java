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

import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/5
 * @since 1.0.0
 */
@Data
public abstract class DataSupport {

    private Integer id;

    private String code;

    private String name;

    private String description;

    /**
     * 工作空间
     */
    private Integer workspaceId;
    /**
     * 工作空间code
     */
    private String workspaceCode;

    /**
     * 版本号
     */
    private String version;

    /**
     * 执行决策表/规则
     *
     * @param input         输入参数
     * @param configuration 配置信息
     * @return 执行结果
     */
    @Nullable
    protected abstract Object execute(@NonNull Input input, @NonNull RuleEngineConfiguration configuration);


}
