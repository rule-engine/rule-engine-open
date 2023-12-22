package cn.ruleengine.web.vo.permission.data;

import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 * 更新数据权限请求参数
 *
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/6/26 4:11 下午
 **/
@Data
public class UpdateDataPermissionRequest {

    private Integer dataType;

    /**
     * 数据库id
     * 如果data_type=0 则此data_id为规则的id
     */
    private Integer dataId;

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
