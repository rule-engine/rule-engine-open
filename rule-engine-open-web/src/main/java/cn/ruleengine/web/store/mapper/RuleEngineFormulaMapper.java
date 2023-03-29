package cn.ruleengine.web.store.mapper;

import cn.ruleengine.core.annotation.Param;
import cn.ruleengine.web.store.entity.RuleEngineFormula;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2021-07-17
 */
public interface RuleEngineFormulaMapper extends BaseMapper<RuleEngineFormula> {

    /**
     * 参数code是否被表达式引用
     *
     * @param code 参数code
     * @return r
     */
    RuleEngineFormula validDataReference(@Param("code") String code);

}
