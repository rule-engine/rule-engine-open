package cn.ruleengine.web.vo.datapermission;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * 数据权限请求参数
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/6/25 7:55 下午
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ListDataPermissionRequest extends DataTypeAndId {


    private String username;

}
