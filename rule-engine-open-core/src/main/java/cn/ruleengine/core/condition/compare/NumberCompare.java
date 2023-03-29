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
package cn.ruleengine.core.condition.compare;


import cn.ruleengine.core.condition.Compare;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.exception.ConditionException;

import java.math.BigDecimal;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/6
 * @since 1.0.0
 */
public class NumberCompare implements Compare {


    private NumberCompare() {

    }

    private static final NumberCompare NUMBER_COMPARE = new NumberCompare();

    public static NumberCompare getInstance() {
        return NUMBER_COMPARE;
    }

    /**
     * number类型条件比较
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
        if (!(leftValue instanceof BigDecimal) || !(rightValue instanceof BigDecimal)) {
            throw new ConditionException("左值/右值必须是BigDecimal");
        }
        BigDecimal lValue = (BigDecimal) leftValue;
        BigDecimal rValue = (BigDecimal) rightValue;
        int compare = lValue.compareTo(rValue);
        switch (operator) {
            case EQ:
                return compare == 0;
            case NE:
                return compare != 0;
            case GT:
                return compare > 0;
            case LT:
                return compare < 0;
            case GE:
                return compare == 0 || compare > 0;
            case LE:
                return compare == 0 || compare < 0;
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
    }

}
