package cn.ruleengine.web.vo.condition;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/12
 * @since 1.0.0
 */
@Data
public class ExecuteConditionRequest {

    /**
     * 条件id
     */
    @NotNull(message = "条件ID不能为空")
    private Integer id;

    /**
     * 运行入参
     */
    private Map<String, Object> params = new HashMap<>();

}
