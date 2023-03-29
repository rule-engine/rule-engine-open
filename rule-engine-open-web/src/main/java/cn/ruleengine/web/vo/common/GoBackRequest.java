package cn.ruleengine.web.vo.common;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@Data
public class GoBackRequest {

    /**
     * 规则、决策表、规则集
     */
    @NotNull
    private Integer dataId;

    /**
     * 回退到哪个版本
     */
    @NotEmpty
    private String version;

}
