package cn.ruleengine.compute.vo;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/22
 * @since 1.0.0
 */
@Data
public class BatchExecuteResponse {


    /**
     * 规则执行状态，是否执行成功，或者遇到了异常
     */
    private Boolean isDone = true;
    /**
     * isDone=false规则执行错误消息
     */
    private String message;

    /**
     * 标记规则使用，防止传入规则与规则输出结果顺序错误时
     * 通过此标记区分
     */
    @Nullable
    private String symbol;

    /**
     * 规则执行结果
     */
    private Object output;

}
