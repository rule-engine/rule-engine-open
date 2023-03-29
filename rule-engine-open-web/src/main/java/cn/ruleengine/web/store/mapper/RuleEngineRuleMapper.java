package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-07-15
 */
public interface RuleEngineRuleMapper extends BaseMapper<RuleEngineRule> {

    /**
     * 根据id更新
     *
     * @param ruleEngineRule 规则信息
     * @return int
     */
    int updateRuleById(RuleEngineRule ruleEngineRule);

}
