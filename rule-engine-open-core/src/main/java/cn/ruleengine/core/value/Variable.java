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
package cn.ruleengine.core.value;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import lombok.Getter;
import lombok.ToString;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@ToString
public class Variable implements Value {

    @Getter
    private Integer variableId;

    /**
     * 变量值类型
     */
    private ValueType valueType;

    /**
     * json反序列化使用
     */
    Variable() {
    }

    public Variable(Integer variableId, ValueType valueType) {
        this.variableId = variableId;
        this.valueType = valueType;
    }

    @Override
    public Object getValue(Input input, RuleEngineConfiguration configuration) {
        Value value = configuration.getEngineVariable().getVariable(this.getVariableId());
        if (value instanceof Constant) {
            Constant constantVal = (Constant) value;
            return constantVal.getValue();
        } else if (value instanceof Formula) {
            Formula formula = (Formula) value;
            return formula.getValue(input, configuration);
        }
        Function functionValue = (Function) value;
        return functionValue.getValue(input, configuration);
    }

    @Override
    public ValueType getValueType() {
        return this.valueType;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Variable)) {
            return false;
        }
        // 如果id一样，则是同一个变量
        return this.variableId.equals(((Variable) obj).variableId);
    }

}
