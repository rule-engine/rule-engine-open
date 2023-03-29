package cn.ruleengine.web.vo.inputparameter;

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
public class GetInputParameterResponse {

    private Integer id;

    private String name;

    private String code;

    private String valueType;

    private String description;

}
