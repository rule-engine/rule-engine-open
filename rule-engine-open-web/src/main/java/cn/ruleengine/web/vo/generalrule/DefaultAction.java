package cn.ruleengine.web.vo.generalrule;


import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/3
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultAction extends ConfigValue {

    public DefaultAction() {

    }

    public DefaultAction(ConfigValue configValue) {
        this.setValue(configValue.getValue());
        this.setType(configValue.getType());
        this.setValueName(configValue.getValueName());
        this.setVariableValue(configValue.getVariableValue());
        this.setValueType(configValue.getValueType());
    }

    /**
     * 0启用 1不启用
     */
    @NotNull
    private Integer enableDefaultAction;


}
