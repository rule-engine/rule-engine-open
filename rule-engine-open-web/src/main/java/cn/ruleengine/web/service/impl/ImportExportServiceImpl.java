package cn.ruleengine.web.service.impl;


import cn.ruleengine.web.enums.DataType;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.ImportExportService;
import cn.ruleengine.web.store.entity.RuleEngineGeneralRulePublish;
import cn.ruleengine.web.store.manager.RuleEngineGeneralRulePublishManager;
import cn.ruleengine.web.vo.data.file.ExportRequest;
import cn.ruleengine.web.vo.data.file.ExportResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈ImportExportServiceImpl〉
 *
 * @author 丁乾文
 * @date 2021/7/13 5:41 下午
 * @since 1.0.0
 */
@Slf4j
@Service
public class ImportExportServiceImpl implements ImportExportService {

    @Resource
    private RuleEngineGeneralRulePublishManager ruleEngineGeneralRulePublishManager;

    /**
     * 导出文件 可执行json文件
     * <p>
     * *.r 为规则文件
     * *.dt 为决策表文件
     * *.rs 为规则集文件
     *
     * @return e
     */
    @Override
    public ExportResponse exportFile(ExportRequest exportRequest) {
        Integer dataId = exportRequest.getDataId();
        String version = exportRequest.getVersion();
        DataType dataType = DataType.getByType(exportRequest.getDataType());
        ExportResponse exportResponse = new ExportResponse();
        exportResponse.setId(dataId);
        switch (dataType) {
            case GENERAL_RULE:
                RuleEngineGeneralRulePublish generalRulePublish = ruleEngineGeneralRulePublishManager.lambdaQuery()
                        .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, dataId)
                        .eq(RuleEngineGeneralRulePublish::getVersion, version)
                        .one();
                if (generalRulePublish == null) {
                    throw new ApiException("没有找到可下载版本数据");
                }
                String data = generalRulePublish.getData();
                exportResponse.setData(data);
                // ...
                exportResponse.setCode(generalRulePublish.getGeneralRuleCode());
                break;
            case RULE_SET:
                break;
            case DECISION_TABLE:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dataType);
        }
        return exportResponse;
    }

}
