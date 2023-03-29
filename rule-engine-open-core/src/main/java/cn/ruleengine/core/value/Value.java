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

import cn.hutool.core.util.NumberUtil;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.condition.compare.BooleanCompare;
import cn.ruleengine.core.condition.compare.DateCompare;
import cn.ruleengine.core.exception.ValueException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/2/29
 * @since 1.0.0
 */
public interface Value {

    /**
     * 获取value
     *
     * @param input         上下文
     * @param configuration 规则配置信息
     * @return value
     */
    Object getValue(Input input, RuleEngineConfiguration configuration);


    /**
     * value 的类型，规则入参，固定值，变量
     *
     * @return type
     */
    ValueType getValueType();

    /**
     * 数据转换
     *
     * @param value     转换的数据
     * @param valueType 数据类型
     * @return 转换后的数据
     */
    default Object dataConversion(Object value, ValueType valueType) {
        Objects.requireNonNull(valueType);
        // 如果为null 或者字符
        if (Objects.isNull(value)) {
            return null;
        }
        // to string , obj.toString()
        String valueString = String.valueOf(value);
        // 根据valueType 解析值 获取对应的类型
        switch (valueType) {
            case COLLECTION:
                /*
                 * 为空时集合返回一个 Collections.emptyList() 而不是 null
                 * <br>
                 * 主要应对：如果集合[1，2，3] CONTAIN [] 返回true
                 */
                if (valueString.isEmpty()) {
                    return Collections.emptyList();
                }
                if (value instanceof Collection) {
                    return value;
                } else {
                    return Arrays.asList(valueString.split(","));
                }
            case NUMBER:
                // 如果为空字符
                if (valueString.isEmpty()) {
                    return null;
                }
                if (value instanceof BigDecimal) {
                    return value;
                }
                if (NumberUtil.isNumber(valueString)) {
                    return new BigDecimal(valueString);
                }
                throw new ValueException(value + "只能是Number类型");
            case STRING:
                // string 类型直接返回
                return valueString;
            case BOOLEAN:
                // 如果为空字符
                if (valueString.isEmpty()) {
                    return null;
                }
                if (value instanceof Boolean) {
                    return value;
                } else if (Objects.equals(valueString, BooleanCompare.TRUE)) {
                    return true;
                } else if (Objects.equals(valueString, BooleanCompare.FALSE)) {
                    return false;
                }
                throw new ValueException(value + "只能是Boolean类型");
            case DATE:
                // 如果为空字符
                if (valueString.isEmpty()) {
                    return null;
                }
                DateCompare.DateTime dateTime = DateCompare.DateTime.of(value);
                if (dateTime != null) {
                    return dateTime;
                }
                throw new ValueException(value + "日期格式错误");
            default:
                throw new ValueException("不支持的数据类型" + valueType);
        }
    }

}
