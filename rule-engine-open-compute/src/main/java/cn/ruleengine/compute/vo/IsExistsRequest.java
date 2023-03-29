package cn.ruleengine.compute.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/13
 * @since 1.0.0
 */
@Data
public class IsExistsRequest {

    @NotBlank
    private String code;
    @NotBlank
    private String workspaceCode;
    @NotBlank
    private String accessKeyId;
    @NotBlank
    private String accessKeySecret;

}
