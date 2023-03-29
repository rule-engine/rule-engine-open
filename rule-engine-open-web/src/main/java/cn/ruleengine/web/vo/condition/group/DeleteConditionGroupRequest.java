package cn.ruleengine.web.vo.condition.group;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 〈DeleteConditionGroupRequest〉
 *
 * @author 丁乾文
 * @date 2021/9/9 4:49 下午
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteConditionGroupRequest extends DataTypeAndId {

    private Integer id;

}
