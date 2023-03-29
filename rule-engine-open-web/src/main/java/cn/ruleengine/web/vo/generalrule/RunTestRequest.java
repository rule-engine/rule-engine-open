package cn.ruleengine.web.vo.generalrule;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Data
public class RunTestRequest {

    /**
     * 规则id
     */
    @NotNull
    private Integer id;

    /**
     * DataStatus
     */
    @NotNull
    private Integer status;

    @NotEmpty
    private String workspaceCode;

    @NotEmpty
    private String code;

    private Map<String, Object> param = new HashMap<>();

}
