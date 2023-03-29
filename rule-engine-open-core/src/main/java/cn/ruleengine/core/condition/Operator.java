/**
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
package cn.ruleengine.core.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/9
 * @since 1.0.0
 */
@AllArgsConstructor
public enum Operator {

    /**
     * EQ 就是 EQUAL等于
     * NE就是 NOT EQUAL不等于
     * GT 就是 GREATER THAN大于
     * LT 就是 LESS THAN小于
     * GE 就是 GREATER THAN OR EQUAL 大于等于
     * LE 就是 LESS THAN OR EQUAL 小于等于
     */
    EQ("等于", "=="),
    NE("不等于", "!="),
    GT("大于", ">"),
    LT("小于", "<"),
    GE("大于等于", ">="),
    LE("小于等于", "<="),

    CONTAIN("包含", "contain"),
    NOT_CONTAIN("不包含", "not contain"),
    IN("在", "in"),
    NOT_IN("不在", "not in"),
    STARTS_WITH("以..开始", "starts with"),
    ENDS_WITH("以..结束", "ends with");

    @Getter
    private final String explanation;
    @Getter
    private final String symbol;

    /**
     * 根据枚举name 获取枚举对象
     *
     * @param name 枚举name
     * @return Operator
     */
    public static Operator getByName(String name) {
        switch (name) {
            case "EQ":
                return EQ;
            case "NE":
                return NE;
            case "GT":
                return GT;
            case "LT":
                return LT;
            case "GE":
                return GE;
            case "LE":
                return LE;
            case "CONTAIN":
                return CONTAIN;
            case "NOT_CONTAIN":
                return NOT_CONTAIN;
            case "IN":
                return IN;
            case "NOT_IN":
                return NOT_IN;
            case "STARTS_WITH":
                return STARTS_WITH;
            case "ENDS_WITH":
                return ENDS_WITH;
            default:
                throw new IllegalStateException("Unexpected value: " + name);
        }
    }

}
