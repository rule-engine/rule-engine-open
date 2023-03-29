package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.service.DataPermissionService;
import cn.ruleengine.web.vo.datapermission.ListDataPermissionRequest;
import cn.ruleengine.web.vo.datapermission.ListDataPermissionResponse;
import cn.ruleengine.web.vo.datapermission.UpdateDataPermissionRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/***
 * 数据权限控制器
 *
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/6/25 7:43 下午
 **/
@Api(tags = "数据权限控制器")
@RestController
@RequestMapping("ruleEngine/dataPermission")
public class DataPermissionController {

    @Resource
    private DataPermissionService dataPermissionService;

    /**
     * 数据权限列表
     *
     * @param pageRequest p
     * @return r
     */
    @PostMapping("list")
    @ApiOperation("数据权限列表")
    public PageResult<ListDataPermissionResponse> list(@RequestBody @Valid PageRequest<ListDataPermissionRequest> pageRequest) {
        return this.dataPermissionService.list(pageRequest);
    }

    /**
     * 保存或者更新数据权限
     *
     * @param updateRequest u
     * @return r
     */
    @PostMapping("saveOrUpdateDataPermission")
    @ApiOperation("更新数据权限")
    public BaseResult update(@RequestBody @Valid UpdateDataPermissionRequest updateRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(dataPermissionService.saveOrUpdateDataPermission(updateRequest));
        return plainResult;
    }

}
