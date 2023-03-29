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

import cn.ruleengine.core.condition.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/9
 * @since 1.0.0
 */
@AllArgsConstructor
public enum ValueType {
    /**
     * 数据类型
     */
    STRING("字符串", "STRING", Arrays.asList(Operator.EQ, Operator.NE, Operator.CONTAIN, Operator.ENDS_WITH, Operator.STARTS_WITH)),
    BOOLEAN("布尔", "BOOLEAN", Arrays.asList(Operator.EQ, Operator.NE)),
    NUMBER("数值", "NUMBER", Arrays.asList(Operator.GT, Operator.LT, Operator.EQ, Operator.NE, Operator.GE, Operator.LE)),
    /**
     * =:两个集合内容相同
     */
    COLLECTION("集合", "COLLECTION", Arrays.asList(Operator.EQ, Operator.IN, Operator.NOT_IN, Operator.CONTAIN, Operator.NOT_CONTAIN)),
    /**
     * 应对网友需求编写增加日期类型
     */
    DATE("日期", "DATE", Arrays.asList(Operator.GT, Operator.LT, Operator.EQ, Operator.NE, Operator.GE, Operator.LE)),

    /**
     * 还没发确定返回值为什么的场景
     */
    UNKNOWN("未知", "UNKNOWN", Collections.emptyList()),

    ;

    @Getter
    private final String name;
    @Getter
    private final String value;
    @Getter
    private final List<Operator> symbol;

    public Class<?> getClassType() {
        switch (value) {
            case "STRING":
                return String.class;
            case "BOOLEAN":
                return Boolean.class;
            case "NUMBER":
                return Number.class;
            case "COLLECTION":
                return Collection.class;
            case "DATE":
                return Date.class;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    public static ValueType getByValue(String value) {
        switch (value) {
            case "STRING":
                return STRING;
            case "BOOLEAN":
                return BOOLEAN;
            case "NUMBER":
                return NUMBER;
            case "COLLECTION":
                return COLLECTION;
            case "DATE":
                return DATE;
            case "UNKNOWN":
                return UNKNOWN;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

}
