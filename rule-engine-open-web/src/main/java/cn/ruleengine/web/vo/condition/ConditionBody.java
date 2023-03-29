package cn.ruleengine.web.vo.condition;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class ConditionBody {

    private Integer id;

    @NotBlank
    private String name;

    private String description;

    @Valid
    private ConfigBean config;

}
