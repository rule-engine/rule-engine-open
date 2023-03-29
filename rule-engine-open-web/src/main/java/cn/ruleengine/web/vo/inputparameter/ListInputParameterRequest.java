package cn.ruleengine.web.vo.inputparameter;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListInputParameterRequest  extends DataTypeAndId {
    private String name;
    private String code;

    private List<String> valueType;
}
