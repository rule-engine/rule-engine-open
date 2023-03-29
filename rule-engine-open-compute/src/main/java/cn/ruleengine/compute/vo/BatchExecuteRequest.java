package cn.ruleengine.compute.vo;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/22
 * @since 1.0.0
 */
@Data
public class BatchExecuteRequest {

    @NotEmpty(message = "工作空间编码不能为空")
    private String workspaceCode;

    @NotEmpty(message = "工作空间AccessKeyId不能为空")
    private String accessKeyId;

    @NotEmpty(message = "工作空间AccessKeySecret不能为空")
    private String accessKeySecret;

    /**
     * 指定一个线程处理多少规则
     */
    @Min(100)
    @Max(2000)
    private Integer threadSegNumber = 100;

    /**
     * 执行超时时间，-1永不超时
     */
    @NotNull
    private Long timeout = -1L;

    /**
     * 规则执行信息，规则code以及规则入参
     */
    @Size(max = 2000)
    @NotNull
    private List<ExecuteInfo> executeInfos;

    @Data
    public static class ExecuteInfo {

        /**
         * 标记规则使用，防止传入规则与规则输出结果顺序错误时
         * 通过此标记区分
         */
        @Nullable
        private String symbol;

        @NotEmpty(message = "规则编码不能为空")
        private String code;

        private Map<String, Object> param = new HashMap<>();

    }

}
