package cn.ruleengine.web.vo.formula;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author dingqianwen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateFormulaRequest extends DataTypeAndId {

    private Integer id;

    private String name;

    private String value;

    private String valueType;

    private String description;

}
