package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineDataReference;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2021-07-27
 */
public interface RuleEngineDataReferenceMapper extends BaseMapper<RuleEngineDataReference> {

    /**
     * 是否有引用这个数据
     *
     * @param type      元素、变量、条件、规则等
     * @param refDataId 元素id...
     * @return r
     */
    RuleEngineDataReference dataReference(@Param("type") Integer type, @Param("refDataId") Integer refDataId);

}
