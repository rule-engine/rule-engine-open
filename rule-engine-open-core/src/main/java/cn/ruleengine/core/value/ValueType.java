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
    STRING("字符串",
            Arrays.asList(Operator.EQ, Operator.NE, Operator.CONTAIN, Operator.ENDS_WITH, Operator.STARTS_WITH),
            String.class),
    BOOLEAN("布尔",
            Arrays.asList(Operator.EQ, Operator.NE),
            Boolean.class),
    NUMBER("数值",
            Arrays.asList(Operator.GT, Operator.LT, Operator.EQ, Operator.NE, Operator.GE, Operator.LE),
            Number.class),
    /**
     * =:两个集合内容相同
     */
    COLLECTION("集合",
            Arrays.asList(Operator.EQ, Operator.IN, Operator.NOT_IN, Operator.CONTAIN, Operator.NOT_CONTAIN),
            Collection.class),
    /**
     * 应对网友需求编写增加日期类型
     */
    DATE("日期",
            Arrays.asList(Operator.GT, Operator.LT, Operator.EQ, Operator.NE, Operator.GE, Operator.LE),
            Date.class),

    /**
     * 还没发确定返回值为什么的场景
     * <p>
     * 变量选择函数时，标记下类型
     */
    UNKNOWN("未知",
            Collections.emptyList(),
            Object.class),

    ;

    @Getter
    private final String name;
    @Getter
    private final List<Operator> symbol;
    @Getter
    private final Class<?> classType;

    public static ValueType getByValue(String value) {
        return ValueType.valueOf(value);
    }

}
