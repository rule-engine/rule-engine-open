package cn.ruleengine.web.vo.formula;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/***
 *
 * @author shangyalong
 * @version 1.0
 * @since 2021/7/31 15:09
 **/
@Data
public class ValidationExpressionNameRequest {

    @NotBlank
    private String name;
}
