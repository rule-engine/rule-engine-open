package cn.ruleengine.web.service;

import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.vo.datapermission.ListDataPermissionRequest;
import cn.ruleengine.web.vo.datapermission.ListDataPermissionResponse;
import cn.ruleengine.web.vo.datapermission.UpdateDataPermissionRequest;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/13
 * @since 1.0.0
 */
public interface DataPermissionService {

    /**
     * 校验数据权限
     *
     * @param id             数据id
     * @param dataPermission dataPermission
     * @return true有权限
     */
    Boolean validDataPermission(Serializable id, DataPermission dataPermission);

    /**
     * 数据权限列表
     *
     * @param pageRequest p
     * @return r
     */
    PageResult<ListDataPermissionResponse> list(PageRequest<ListDataPermissionRequest> pageRequest);


    /**
     * 保存或者更新数据权限
     *
     * @param updateRequest u
     * @return Integer
     */
    Boolean saveOrUpdateDataPermission(UpdateDataPermissionRequest updateRequest);
}
