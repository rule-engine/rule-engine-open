package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.*;
import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.enums.DataType;
import cn.ruleengine.web.enums.OperationType;
import cn.ruleengine.web.service.FormulaService;
import cn.ruleengine.web.vo.formula.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 〈FormulaController〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @see cn.ruleengine.core.value.Formula
 * @since 1.0.0
 */
@Api(tags = "表达式控制器")
@RestController
@RequestMapping("ruleEngine/formula")
public class FormulaController {

    @Resource
    private FormulaService formulaService;


    /**
     * 表达式列表
     *
     * @param pageRequest param
     * @return r
     */
    @PostMapping("list")
    @ApiOperation("表达式列表")
    public PageResult<FormulaListResponse> list(@RequestBody @Valid PageRequest<FormulaListRequest> pageRequest) {
        return this.formulaService.list(pageRequest);
    }


    /**
     * 保存表达式
     *
     * @param saveFormulaRequest param
     * @return r
     */
    @PostMapping("saveFormula")
    @ApiOperation("保存表达式")
    public PlainResult<Boolean> saveFormula(@RequestBody @Valid SaveFormulaRequest saveFormulaRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(this.formulaService.saveFormula(saveFormulaRequest));
        return plainResult;
    }


    /**
     * 更新表达式
     *
     * @param updateFormulaRequest param
     * @return r
     */
    @DataPermission(id = "#updateFormulaRequest.id", dataType = DataType.FORMULA, operationType = OperationType.UPDATE)
    @PostMapping("updateFormula")
    @ApiOperation("更新表达式")
    public PlainResult<Boolean> updateFormula(@RequestBody @Valid UpdateFormulaRequest updateFormulaRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(this.formulaService.updateFormula(updateFormulaRequest));
        return plainResult;
    }


    /**
     * downloadList
     * 获取规则定义信息
     *
     * @param idRequest 规则id
     * @return 规则定义信息
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.FORMULA, operationType = OperationType.SELECT)
    @PostMapping("getFormula")
    @ApiOperation("查询表达式信息")
    public BaseResult getFormula(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<GetFormulaResponse> plainResult = new PlainResult<>();
        plainResult.setData(this.formulaService.getFormula(idRequest.getId()));
        return plainResult;
    }

    /**
     * 删除表达式
     *
     * @param idRequest id
     * @return r
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.FORMULA, operationType = OperationType.DELETE)
    @PostMapping("deleteFormula")
    @ApiOperation("删除表达式")
    public BaseResult deleteFormula(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(this.formulaService.deleteFormula(idRequest.getId()));
        return plainResult;

    }

    /**
     * 验证表达式名称是否存在
     *
     * @param validationExpressionNameRequest v
     * @return r true 表示存在
     */
    @PostMapping("validationExpressionName")
    @ApiOperation("验证表达式名称是否存在")
    public BaseResult validationExpressionName(@RequestBody @Valid ValidationExpressionNameRequest validationExpressionNameRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(this.formulaService.validationExpressionName(validationExpressionNameRequest));
        return plainResult;
    }

}
