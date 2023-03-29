package cn.ruleengine.web.store.mapper;


import cn.ruleengine.core.annotation.Param;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroupCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-07-16
 */
public interface RuleEngineConditionGroupConditionMapper extends BaseMapper<RuleEngineConditionGroupCondition> {

    /**
     * 更新条件顺序 +1
     * toOrderNo 大于它的统统加1
     *
     * @param toConditionGroupId to条件组id
     * @param toOrderNo          to
     */
    void updateConditionOrderGreaterThanPlus(@Param("toConditionGroupId") Integer toConditionGroupId, @Param("toOrderNo") Integer toOrderNo);


    /**
     * 更新条件顺序 -1
     * toOrderNo 小于它的统统减1
     *
     * @param toConditionGroupId to条件组id
     * @param toOrderNo          to
     */
    void updateConditionOrderLessThanMinus(@Param("toConditionGroupId") Integer toConditionGroupId, @Param("toOrderNo") Integer toOrderNo);

}
