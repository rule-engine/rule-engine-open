package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.data.file.ExportRequest;
import cn.ruleengine.web.vo.data.file.ExportResponse;

/**
 * 〈ImportExportService〉
 *
 * @author 丁乾文
 * @date 2021/7/13 5:40 下午
 * @since 1.0.0
 */
public interface ImportExportService {

    /**
     * 导出文件 可执行json文件
     * <p>
     * *.r 为规则文件
     * *.dt 为决策表文件
     * *.rs 为规则集文件
     *
     * @param exportRequest e
     * @return e
     */
    ExportResponse exportFile(ExportRequest exportRequest);
}
