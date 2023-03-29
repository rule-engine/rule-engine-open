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
package cn.ruleengine.core.rule;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.JsonParse;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.condition.ConditionSet;
import cn.ruleengine.core.value.Value;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/9
 * @since 1.0.0
 */
@Slf4j
@Data
public class Rule implements JsonParse {

    private Integer id;

    private String code;

    private String name;

    private String description;

    /**
     * 当条件全部满足时候返回此规则结果
     */
    private ConditionSet conditionSet = new ConditionSet();
    /**
     * 返回结果
     */
    private Value actionValue;

    /**
     * 执行规则
     *
     * @param input         入参
     * @param configuration 规则引擎配置
     * @return 规则返回值
     */
    @Nullable
    public Object execute(@NonNull Input input, @NonNull RuleEngineConfiguration configuration) {
        // 比较规则条件集
        if (this.conditionSet.compare(input, configuration)) {
            // 条件全部命中时候执行
            return this.getActionValue().getValue(input, configuration);
        }
        return null;
    }

    /**
     * 根据rule json字符串构建一个规则
     *
     * @param jsonString rule json字符串
     * @return rule
     */
    @SneakyThrows
    public static Rule buildRule(@NonNull String jsonString) {
        return OBJECT_MAPPER.readValue(jsonString, Rule.class);
    }

    @SneakyThrows
    @Override
    public void fromJson(@NonNull String jsonString) {
        Rule rule = Rule.buildRule(jsonString);
        this.setId(rule.getId());
        this.setCode(rule.getCode());
        this.setName(rule.getName());
        this.setDescription(rule.getDescription());
        this.setConditionSet(rule.getConditionSet());
        this.setActionValue(rule.getActionValue());
    }


    public void setConditionSet(ConditionSet conditionSet) {
        this.conditionSet = Objects.requireNonNull(conditionSet);
    }


}
