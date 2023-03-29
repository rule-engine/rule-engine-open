package cn.ruleengine.web.vo.function;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Data
public class FunctionParam {
    private String name;

    private String code;

    /**
     * STRING,BOOLEAN...
     */
    private String valueType;
}
