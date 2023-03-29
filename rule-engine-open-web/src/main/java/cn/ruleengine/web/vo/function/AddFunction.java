package cn.ruleengine.web.vo.function;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/11
 * @since 1.0.0
 */
@Data
public class AddFunction {

    /**
     * 函数java代码
     */
    @NotBlank
    private String javaCode;
    /**
     * 类名称
     */
    @NotBlank
    private String className;

    @NotBlank
    private String name;

    private String description;

    private List<FunctionParam> param;

}
