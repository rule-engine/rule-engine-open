package cn.ruleengine.web.vo.datapermission;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/***
 * 更新数据权限请求参数
 *
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/6/26 4:11 下午
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateDataPermissionRequest extends DataTypeAndId {

    /**
     * 0有写权限
     */
    @NotNull
    private Integer writeAuthority;

    /**
     * 0有发布规则权限
     */
    @NotNull
    private Integer publishAuthority;

    /**
     * 用户id
     */
    @NotNull
    private Integer userId;
}
