package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.IdRequest;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.annotation.Auth;
import cn.ruleengine.web.service.SystemLogService;
import cn.ruleengine.web.vo.system.log.GetLogResponse;
import cn.ruleengine.web.vo.system.log.ListLogRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/3/2
 * @since 1.0.0
 */
@Api(tags = "系统日志控制器")
@RestController
@RequestMapping("system/log")
public class SystemLogController {

    @Resource
    private SystemLogService systemLogService;


    /**
     * 查询日志列表
     *
     * @param pageRequest 分页参数
     * @return list
     */
    @PostMapping("list")
    @ApiOperation("日志列表")
    public BaseResult list(@RequestBody PageRequest<ListLogRequest> pageRequest) {
        return systemLogService.list(pageRequest);
    }

    /**
     * 根据id查询日志详情
     *
     * @param idRequest 日志id
     * @return info
     */
    @PostMapping("get")
    @ApiOperation("根据id查询日志详情")
    public PlainResult<GetLogResponse> get(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<GetLogResponse> plainResult = new PlainResult<>();
        plainResult.setData(systemLogService.get(idRequest.getId()));
        return plainResult;
    }

    /**
     * 根据id删除日志，只能由管理删除
     *
     * @param idRequest 日志id
     * @return true
     */
    @Auth
    @PostMapping("delete")
    @ApiOperation("根据id删除日志")
    public PlainResult<Boolean> delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(systemLogService.delete(idRequest.getId()));
        return plainResult;
    }

}
