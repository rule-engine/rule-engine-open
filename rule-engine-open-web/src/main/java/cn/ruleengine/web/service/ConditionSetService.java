package cn.ruleengine.web.service;


import cn.ruleengine.core.condition.ConditionSet;
import cn.ruleengine.web.vo.condition.ConditionGroupConfig;

import java.util.List;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/1/17
 * @since 1.0.0
 */
public interface ConditionSetService {


    /**
     * 获取规则配置条件集，懒得写的，待优化
     *
     * @param conditionGroup 条件组配置
     * @return 条件集
     */
    ConditionSet loadConditionSet(List<ConditionGroupConfig> conditionGroup);

    /**
     * 获取规则配置条件集，懒得写的，待优化
     *
     * @param ruleId 规则id
     * @return 条件集
     */
    ConditionSet loadConditionSet(Integer ruleId);

}

