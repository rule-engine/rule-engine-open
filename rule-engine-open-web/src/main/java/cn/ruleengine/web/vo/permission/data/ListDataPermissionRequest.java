package cn.ruleengine.web.vo.permission.data;

import lombok.Data;

/***
 * 数据权限请求参数
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/6/25 7:55 下午
 **/
@Data
public class ListDataPermissionRequest {

    private Integer dataType;

    /**
     * 数据库id
     * 如果data_type=0 则此data_id为规则的id
     */
    private Integer dataId;

    private String username;

}
