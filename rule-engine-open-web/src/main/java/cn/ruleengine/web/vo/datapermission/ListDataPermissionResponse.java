package cn.ruleengine.web.vo.datapermission;

import lombok.Data;

/***
 * 数据权限请求参数
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/6/25 7:55 下午
 **/
@Data
public class ListDataPermissionResponse {

    private String username;

    private String email;

    private String avatar;

    private Integer userId;


    /**
     * 0有写权限
     */
    private Integer writeAuthority;

    /**
     * 0有发布规则权限
     */
    private Integer publishAuthority;

}
