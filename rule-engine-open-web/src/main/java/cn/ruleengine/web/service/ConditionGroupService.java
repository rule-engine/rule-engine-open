package cn.ruleengine.web.service;

import cn.ruleengine.core.condition.ConditionGroup;
import cn.ruleengine.web.vo.common.RearrangeRequest;
import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.group.SaveOrUpdateConditionGroup;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
public interface ConditionGroupService {

    /**
     * 保存或者更新条件组
     *
     * @param saveOrUpdateConditionGroup 条件组信息
     * @return int
     */
    Integer saveOrUpdateConditionGroup(SaveOrUpdateConditionGroup saveOrUpdateConditionGroup);

    /**
     * 保存条件组
     *
     * @param ruleId         规则id
     * @param conditionGroup 条件组信息
     */
    void saveConditionGroup(Integer ruleId, List<ConditionGroupConfig> conditionGroup);

    /**
     * 删除条件组
     *
     * @param id 条件组id
     * @return true
     */
    Boolean delete(Integer id);

    /**
     * 删除规则条件组信息
     *
     * @param ruleIds 规则ids
     */
    void removeConditionGroupByRuleIds(List<Integer> ruleIds);

    /**
     * 获取条件组配置
     *
     * @param ruleId 规则id
     * @return ConditionGroupConfig list
     */
    List<ConditionGroupConfig> getConditionGroupConfig(Integer ruleId);

    /**
     * 处理ConditionGroupConfig
     *
     * @param conditionGroups g
     * @return r
     */
    List<ConditionGroupConfig> pressConditionGroupConfig(List<ConditionGroup> conditionGroups);

    /**
     * 重新排序
     *
     * @param rearrangeRequests r
     * @return true
     */
    Boolean rearrange(List<RearrangeRequest> rearrangeRequests);


}
