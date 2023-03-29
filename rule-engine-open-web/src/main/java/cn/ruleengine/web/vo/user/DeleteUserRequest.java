package cn.ruleengine.web.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 删除用户
 *
 * @author : zhj
 * @date : 2021/6/23 21:46
 **/
@Data
public class DeleteUserRequest {

    @ApiModelProperty("用户id")
    @NotNull
    private Integer id;

}
