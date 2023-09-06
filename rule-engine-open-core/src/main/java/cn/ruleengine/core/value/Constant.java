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

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.Input;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@ToString
public class Constant implements Value {


    /**
     * 常量值
     */
    @Getter
    private Object value;

    /**
     * 值类型
     */
    private ValueType valueType;

    /**
     * 反序列化json使用
     */
    Constant() {

    }

    public Constant(@Nullable Object value, @NonNull ValueType valueType) {
        Objects.requireNonNull(valueType);
        // 初始化值
        this.value = this.dataConversion(value, valueType);
        this.valueType = valueType;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Constant)) {
            return false;
        }
        Constant constant = (Constant) other;
        if (this.getValueType() != constant.getValueType()) {
            return false;
        }
        Object curValue = dataConversion(this.value, getValueType());
        Object constantValue = dataConversion(constant.getValue(), this.getValueType());
        switch (constant.getValueType()) {
            case NUMBER:
                if (((Number) curValue).longValue() != ((Number) constantValue).longValue()) {
                    return false;
                }
                break;
            case COLLECTION:
                Collection<?> curValueColl = (Collection<?>) curValue;
                Collection<?> constantValueColl = (Collection<?>) constantValue;
                if (curValueColl.size() != constantValueColl.size()) {
                    return false;
                }
                if (!CollUtil.containsAll(curValueColl, constantValueColl)) {
                    return false;
                }
                break;
            case STRING:
            case BOOLEAN:
                if (!Objects.equals(this.value, constant.getValue())) {
                    return false;
                }
                break;
            case DATE:
                return Objects.equals(curValue, constantValue);
            default:
                throw new IllegalStateException("Unexpected value: " + constant.getValueType());
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, valueType);
    }

    @Override
    public ValueType getValueType() {
        return this.valueType;
    }

    @Override
    public Object getValue(Input input, RuleEngineConfiguration configuration) {
        return this.getValue();
    }

}
