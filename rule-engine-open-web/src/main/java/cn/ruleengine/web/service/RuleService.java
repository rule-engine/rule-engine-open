package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.rule.general.SaveActionRequest;
import cn.ruleengine.web.vo.rule.RuleBody;


/**
 * 〈RuleService〉
 *
 * @author 丁乾文
 * @date 2021/7/28 1:08 下午
 * @since 1.0.0
 */
public interface RuleService {


    /**
     * 保存结果
     *
     * @param saveActionRequest 保存结果
     * @return 保存结果
     */
    Boolean saveAction(SaveActionRequest saveActionRequest);

    /**
     * 保存规则并返回规则id
     *
     * @param ruleBody 规则体
     * @return 规则id
     */
    Integer saveOrUpdateRule(RuleBody ruleBody);


}
