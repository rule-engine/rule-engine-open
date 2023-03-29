package cn.ruleengine.compute.service;

import cn.ruleengine.core.rule.GeneralRule;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/4
 * @since 1.0.0
 */
public interface GeneralRulePublishService {

    /**
     * 获取所有的线上规则
     *
     * @return rule
     */
    List<GeneralRule> getAllPublishGeneralRule();

    /**
     * 根据规则code，查询发布规则
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      规则code
     * @return 规则
     */
    GeneralRule getPublishGeneralRule(String workspaceCode, String ruleCode);

}
