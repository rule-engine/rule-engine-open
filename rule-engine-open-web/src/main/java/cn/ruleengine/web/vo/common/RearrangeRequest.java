package cn.ruleengine.web.vo.common;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author Administrator
 */
@Data
public class RearrangeRequest {

    @NotNull
    private Integer id;
    @NotNull
    private Integer orderNo;

}
