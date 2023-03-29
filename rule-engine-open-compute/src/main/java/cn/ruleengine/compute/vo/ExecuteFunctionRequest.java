package cn.ruleengine.compute.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/12
 * @since 1.0.0
 */
@Data
public class ExecuteFunctionRequest {

    @NotNull(message = "函数ID不能为空")
    private Integer id;

    /**
     * 运行入参
     */
    private List<ParamValue> paramValues = new ArrayList<>();

}
