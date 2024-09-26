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
package cn.ruleengine.core.condition.compare;


import cn.hutool.core.util.StrUtil;
import cn.ruleengine.core.condition.Compare;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.exception.ConditionException;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/6
 * @since 1.0.0
 */
public class StringCompare implements Compare {


    private StringCompare() {

    }

    private static final StringCompare STRING_COMPARE = new StringCompare();

    public static StringCompare getInstance() {
        return STRING_COMPARE;
    }

    /**
     * String类型条件比较
     *
     * @param leftValue  条件左值
     * @param operator   比较符号
     * @param rightValue 条件右值
     * @return true条件成立
     */
    @Override
    public boolean compare(Object leftValue, Operator operator, Object rightValue) {
        if (leftValue == null || rightValue == null) {
            return false;
        }
        if (!(leftValue instanceof String) || !(rightValue instanceof String)) {
            throw new ConditionException("左值/右值必须是String");
        }
        String leftValueStr = (String) leftValue;
        String rightValueStr = (String) rightValue;
        switch (operator) {
            case EQ:
                return leftValue.equals(rightValue);
            case NE:
                return !leftValue.equals(rightValue);
            case CONTAIN:
                return leftValueStr.contains(rightValueStr);
            case NOT_CONTAIN:
                return !leftValueStr.contains(rightValueStr);
            case STARTS_WITH:
                // 如果右值为空，则返回false
                if (StrUtil.isEmpty(rightValueStr)) {
                    return false;
                }
                return leftValueStr.startsWith(rightValueStr);
            case ENDS_WITH:
                // 如果右值为空，则返回false
                if (StrUtil.isEmpty(rightValueStr)) {
                    return false;
                }
                return leftValueStr.endsWith(rightValueStr);
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
    }

}
