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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@AllArgsConstructor
public enum VariableType {

    /**
     * VARIABLE,INPUT_PARAMETER,CONSTANT,FUNCTION
     */
    INPUT_PARAMETER(0), VARIABLE(1), CONSTANT(2), FUNCTION(3),
    /**
     * 表达式
     *
     * @see Formula
     */
    FORMULA(4),
    /**
     * 引用的普通规则
     */
    GENERAL_RULE(10);

    @Getter
    private final Integer type;


    public static VariableType getByType(Integer type) {
        switch (type) {
            case 0:
                return INPUT_PARAMETER;
            case 1:
                return VARIABLE;
            case 2:
                return CONSTANT;
            case 3:
                return FUNCTION;
            case 4:
                return FORMULA;
            case 10:
                return GENERAL_RULE;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

}
