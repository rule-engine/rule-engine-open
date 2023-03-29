package cn.ruleengine.web.vo.workspace;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/***
 * 验证工作空间code
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/7/14 4:26 下午
 **/
@Data
public class VerifyWorkspaceRequest {
    @NotBlank
    @ApiModelProperty(value = "工作空间code", required = true)
    private String code;
}
