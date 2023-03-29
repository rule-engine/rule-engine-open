package cn.ruleengine.web.vo.variable;

import lombok.Data;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/25
 * @since 1.0.0
 */
@Data
public class GetVariableResponse {

    private Integer id;

    private String name;

    private String description;

    private Integer type;

    private String valueType;

    private String value;

    private VariableFunction function;

}
