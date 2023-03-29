package cn.ruleengine.web.vo.formula;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FormulaListRequest extends DataTypeAndId {

    private String name;
    private String code;

    private List<String> valueType;

}
