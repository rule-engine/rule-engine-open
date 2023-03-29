package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.service.ImportExportService;
import cn.ruleengine.web.vo.importexport.ExportRequest;
import cn.ruleengine.web.vo.importexport.ExportResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 〈ImportExportController〉
 *
 * @author 丁乾文
 * @date 2021/6/18 2:42 下午
 * @since 1.0.0
 */
@Api(tags = "导入导出控制器")
@RestController
@Validated
@RequestMapping("ruleEngine/importExport")
public class ImportExportController {

    @Resource
    private ImportExportService importExportService;

    /**
     * 导出文件 可执行json文件
     * <p>
     * *.r 为规则文件
     * *.dt 为决策表文件
     * *.rs 为规则集文件
     */
    @PostMapping("export")
    @ApiOperation("导出文件")
    public PlainResult<ExportResponse> exportFile(@RequestBody @Valid ExportRequest exportRequest) {
        PlainResult<ExportResponse> plainResult = new PlainResult<>();
        plainResult.setData(importExportService.exportFile(exportRequest));
        return plainResult;
    }

    /**
     * 导入文件 可执行json文件
     * <p>
     * *.r 为规则文件
     * *.dt 为决策表文件
     * *.rs 为规则集文件
     */
    @PostMapping("import")
    @ApiOperation("导入文件")
    public void importFile() {

    }

}
