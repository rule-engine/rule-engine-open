package cn.ruleengine.web.vo.workspace;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈UpdateAccessKeyRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/25 4:43 下午
 * @since 1.0.0
 */
@Data
public class UpdateAccessKeyRequest {

    @NotNull
    private Integer id;

    @NotBlank
    private String accessKeyId;

    @NotBlank
    private String accessKeySecret;

}
