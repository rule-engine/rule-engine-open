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
package cn.ruleengine.core.condition;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/9
 * @since 1.0.0
 */
@ToString
@Slf4j
public class ConditionSet implements ConditionCompare {

    /**
     * 条件
     */
    private final List<ConditionGroup> conditionGroups = new ArrayList<>();


    /**
     * 条件集运算，条件组与条件组为｜｜（或关系）
     *
     * @param input         入参
     * @param configuration 引擎配置信息
     * @return 返回true时，所有条件全部成立
     */
    @Override
    public boolean compare(Input input, RuleEngineConfiguration configuration) {
        if (CollUtil.isEmpty(this.conditionGroups)) {
            if (log.isDebugEnabled()) {
                log.debug("条件组为空，没有条件，返回True");
            }
            return true;
        }
        for (int index = 0; index < this.conditionGroups.size(); index++) {
            if (log.isDebugEnabled()) {
                log.debug("开始验证条件组:" + index);
            }
            //条件组是或者关系，有一个为true,直接返回
            if (this.conditionGroups.get(index).compare(input, configuration)) {
                if (log.isDebugEnabled()) {
                    log.debug("条件组:{}成立", index);
                }
                return true;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("条件组:{}不成立", index);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("所有条件不成立");
        }
        return false;
    }

    public void addConditionGroup(@NonNull ConditionGroup conditionGroup) {
        Objects.requireNonNull(conditionGroup);
        this.conditionGroups.add(conditionGroup);
    }

    public List<ConditionGroup> getConditionGroups() {
        return Collections.unmodifiableList(conditionGroups);
    }

}
