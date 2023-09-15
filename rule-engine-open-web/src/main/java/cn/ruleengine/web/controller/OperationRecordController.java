package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.service.OperationRecordService;
import cn.ruleengine.web.vo.operation.record.OperationRecordRequest;
import cn.ruleengine.web.vo.operation.record.OperationRecordResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 〈OperationRecordController〉
 *
 * @author 丁乾文
 * @date 2021/9/9 5:25 下午
 * @since 1.0.0
 */
@Api(tags = "工作台控制器")
@RestController
@RequestMapping("ruleEngine/operationRecord")
public class OperationRecordController {

    @Resource
    private OperationRecordService operationRecordService;

    /**
     * 操作记录
     *
     * @param recordRequestPageRequest r
     * @return r
     */
    @PostMapping("/list")
    @ApiOperation("操作记录")
    public PageResult<OperationRecordResponse> operationRecord(PageRequest<OperationRecordRequest> recordRequestPageRequest) {
        return this.operationRecordService.operationRecord(recordRequestPageRequest);
    }

}
