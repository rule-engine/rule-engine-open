package cn.ruleengine.web.service;

import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.vo.operation.record.OperationRecordRequest;
import cn.ruleengine.web.vo.operation.record.OperationRecordResponse;

/**
 * 〈OperationRecordService〉
 *
 * @author 丁乾文
 * @date 2021/9/9 3:57 下午
 * @since 1.0.0
 */
public interface OperationRecordService {

    /**
     * 保存操作记录
     *
     * @param description 描述
     * @param dataId      id
     * @param dataType    type
     */
    void save(String description, Integer dataId, Integer dataType);

    /**
     * 操作记录
     *
     * @param recordRequestPageRequest r
     * @return r
     */
    PageResult<OperationRecordResponse> operationRecord(PageRequest<OperationRecordRequest> recordRequestPageRequest);

}
