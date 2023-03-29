package cn.ruleengine.web.vo.variable;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/30
 * @since 1.0.0
 */
@Data
public class ParamValue {

    private String name;

    @NotBlank
    private String code;

    /**
     * 规则参数/变量/固定值
     */
    @NotNull
    private Integer type;

    @NotNull
    private String value;

    /**
     * STRING,BOOLEAN...
     */
    @NotBlank
    private String valueType;

    private String valueName;

}
