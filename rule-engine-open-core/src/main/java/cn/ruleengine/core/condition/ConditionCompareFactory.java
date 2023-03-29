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

import cn.ruleengine.core.condition.compare.*;
import cn.ruleengine.core.value.ValueType;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/3
 * @since 1.0.0
 */
public class ConditionCompareFactory {

    /**
     * 根据dataType获取对应的条件比较器
     *
     * @param valueType 数据类型
     * @return Compare
     */
    public static Compare getCompare(@NonNull ValueType valueType) {
        switch (valueType) {
            case NUMBER:
                return NumberCompare.getInstance();
            case BOOLEAN:
                return BooleanCompare.getInstance();
            case STRING:
                return StringCompare.getInstance();
            case COLLECTION:
                return CollectionCompare.getInstance();
            case DATE:
                return DateCompare.getInstance();
            default:
                throw new IllegalStateException("Unexpected value: " + valueType);
        }
    }

}
