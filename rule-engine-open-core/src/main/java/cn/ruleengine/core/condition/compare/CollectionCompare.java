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


import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.condition.Compare;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.exception.ConditionException;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/6
 * @since 1.0.0
 */
public class CollectionCompare implements Compare {


    private CollectionCompare() {

    }

    private static final CollectionCompare COLLECTION_COMPARE = new CollectionCompare();

    public static CollectionCompare getInstance() {
        return COLLECTION_COMPARE;
    }

    /**
     * collection类型条件比较
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
        if (!(leftValue instanceof Collection)) {
            throw new ConditionException("左值必须是Collection");
        }
        Collection<?> leftValueColl = (Collection<?>) leftValue;
        switch (operator) {
            case EQ:
                if (!(rightValue instanceof Collection)) {
                    throw new ConditionException("COLLECTION运算符:EQ,右值只能为集合");
                }
                Collection<?> rightValueColl = (Collection<?>) rightValue;
                if (leftValueColl.size() != rightValueColl.size()) {
                    return false;
                }
                if (leftValueColl == rightValueColl) {
                    return true;
                }
                Iterator<?> leftValueIterator = leftValueColl.iterator();
                Iterator<?> rightValueIterator = rightValueColl.iterator();
                while (leftValueIterator.hasNext() && rightValueIterator.hasNext()) {
                    Object leftValueElement = leftValueIterator.next();
                    Object rightValueElement = rightValueIterator.next();
                    // 依次对比
                    if (!Objects.equals(leftValueElement, rightValueElement)) {
                        return false;
                    }
                }
                return true;
            case CONTAIN:
                if (rightValue instanceof Collection) {
                    return containsAll(leftValueColl, rightValue);
                }
                return leftValueColl.contains(String.valueOf(rightValue));
            case NOT_CONTAIN:
                if (rightValue instanceof Collection) {
                    return !containsAll(leftValueColl, rightValue);
                }
                return !leftValueColl.contains(String.valueOf(rightValue));
            case IN:
                if (!(rightValue instanceof Collection)) {
                    throw new ConditionException("COLLECTION运算符:IN,右值只能为集合");
                }
                return containsAll(rightValue, leftValueColl);
            case NOT_IN:
                if (!(rightValue instanceof Collection)) {
                    throw new ConditionException("COLLECTION运算符:NOT IN,右值只能为集合");
                }
                return !containsAll(rightValue, leftValueColl);
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
    }

    /**
     * 集合1中是否包含集合2中所有的元素，即集合2是否为集合1的子集
     *
     * @param collObj1 集合1
     * @param collObj2 集合2
     * @return 集合1中是否包含集合2中所有的元素
     */
    private boolean containsAll(Object collObj1, Object collObj2) {
        Collection<?> coll1 = (Collection<?>) collObj1;
        Collection<?> coll2 = (Collection<?>) collObj2;
        if (CollUtil.isEmpty(coll1)) {
            return CollUtil.isEmpty(coll2);
        }
        if (CollUtil.isEmpty(coll2)) {
            return true;
        }
        for (Object object : coll2) {
            if (!coll1.contains(object)) {
                return false;
            }
        }
        return true;
    }

}
