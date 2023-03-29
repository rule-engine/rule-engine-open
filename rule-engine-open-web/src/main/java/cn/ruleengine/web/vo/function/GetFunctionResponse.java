package cn.ruleengine.web.vo.function;

import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Data
public class GetFunctionResponse {

    private Integer id;

    private String name;

    private String executor;

    private String description;

    /**
     * 函数返回值
     */
    private String returnValueType;

    /**
     * 函数中所有的参数
     */
    private List<FunctionParam> params;

}
