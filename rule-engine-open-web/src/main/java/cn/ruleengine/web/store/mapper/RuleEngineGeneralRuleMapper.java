package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineGeneralRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-12-29
 */
public interface RuleEngineGeneralRuleMapper extends BaseMapper<RuleEngineGeneralRule> {

    /**
     * 根据id更新
     *
     * @param ruleEngineGeneralRule 规则信息
     */
    void updateRuleById(RuleEngineGeneralRule ruleEngineGeneralRule);

}
