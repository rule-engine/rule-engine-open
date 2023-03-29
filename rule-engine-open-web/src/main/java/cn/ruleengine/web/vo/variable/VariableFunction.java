package cn.ruleengine.web.vo.variable;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 〈VariableFunction〉
 *
 * @author 丁乾文
 * @date 2021/8/2 7:07 下午
 * @since 1.0.0
 */
@Data
public class VariableFunction {

    private Integer id;

    private String name;
    /**
     * 函数中所有的参数
     */
    @Valid
    @NotNull
    private List<ParamValue> paramValues;

    private String returnValueType;

}
