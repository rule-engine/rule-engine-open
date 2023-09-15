package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.core.value.Formula;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.DataReferenceService;
import cn.ruleengine.web.service.FormulaService;
import cn.ruleengine.web.store.entity.RuleEngineFormula;
import cn.ruleengine.web.store.entity.RuleEngineInputParameter;
import cn.ruleengine.web.store.manager.RuleEngineFormulaManager;
import cn.ruleengine.web.store.manager.RuleEngineInputParameterManager;
import cn.ruleengine.web.util.OrikaBeanMapper;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.formula.*;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class FormulaServiceImpl implements FormulaService {

    @Resource
    private RuleEngineFormulaManager ruleEngineFormulaManager;
    @Resource
    private DataReferenceService dataReferenceService;
    @Resource
    private RuleEngineInputParameterManager ruleEngineInputParameterManager;

    /**
     * 验证表达式是否重复
     *
     * @param name name
     * @return Boolean
     */
    private Boolean verifyName(String name, Integer workspaceId) {
        return this.ruleEngineFormulaManager.lambdaQuery()
                .eq(RuleEngineFormula::getName, name)
                .eq(RuleEngineFormula::getWorkspaceId, workspaceId)
                .exists();
    }

    /**
     * 表达式列表
     *
     * @param pageRequest param
     * @return r
     */
    @Override
    public PageResult<FormulaListResponse> list(PageRequest<FormulaListRequest> pageRequest) {
        FormulaListRequest query = pageRequest.getQuery();
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(ruleEngineFormulaManager, page, () -> {
            QueryWrapper<RuleEngineFormula> wrapper = new QueryWrapper<>();
            PageUtils.defaultOrder(orders, wrapper);
            LambdaQueryWrapper<RuleEngineFormula> lambdaQueryWrapper = wrapper.lambda().eq(RuleEngineFormula::getWorkspaceId, workspace.getId());
            if (CollUtil.isNotEmpty(query.getValueType())) {
                lambdaQueryWrapper.in(RuleEngineFormula::getValueType, query.getValueType());
            }
            if (Validator.isNotEmpty(query.getName())) {
                lambdaQueryWrapper.like(RuleEngineFormula::getName, query.getName());
            }
            if (query.getDataId() != null) {
                lambdaQueryWrapper.eq(RuleEngineFormula::getDataId, query.getDataId());
            }
            if (query.getDataType() != null) {
                lambdaQueryWrapper.eq(RuleEngineFormula::getDataType, query.getDataType());
            }
            return wrapper;
        }, (v) -> OrikaBeanMapper.map(v, FormulaListResponse.class));
    }

    /**
     * 保存表达式
     *
     * @param saveFormulaRequest param
     * @return r
     */
    @Override
    public Boolean saveFormula(SaveFormulaRequest saveFormulaRequest) {
        Formula.ExpressionProcessor formulaProcessor = new Formula.ExpressionProcessor(saveFormulaRequest.getValue());
        Set<String> inputParameterCodes = formulaProcessor.getInputParameterCodes();
        // 获取当前用户所在工作区
        Workspace workspace = Context.getCurrentWorkspace();
        if (this.verifyName(saveFormulaRequest.getName(), workspace.getId())) {
            throw new ValidationException("表达式已存在!");
        }
        this.validFormulaParam(inputParameterCodes);
        RuleEngineFormula ruleEngineFormula = new RuleEngineFormula();
        ruleEngineFormula.setName(saveFormulaRequest.getName());
        ruleEngineFormula.setValue(saveFormulaRequest.getValue());
        ruleEngineFormula.setWorkspaceId(workspace.getId());
        ruleEngineFormula.setValueType(saveFormulaRequest.getValueType());
        ruleEngineFormula.setDescription(saveFormulaRequest.getDescription());
        ruleEngineFormula.setReferenceParameterCode(JSON.toJSONString(inputParameterCodes));
        ruleEngineFormula.setDataType(saveFormulaRequest.getDataType());
        ruleEngineFormula.setDataId(saveFormulaRequest.getDataId());
        return this.ruleEngineFormulaManager.save(ruleEngineFormula);
    }


    /**
     * 更新表达式
     *
     * @param updateFormulaRequest param
     * @return r
     */
    @Override
    public Boolean updateFormula(UpdateFormulaRequest updateFormulaRequest) {
        Formula.ExpressionProcessor formulaProcessor = new Formula.ExpressionProcessor(updateFormulaRequest.getValue());
        Set<String> inputParameterCodes = formulaProcessor.getInputParameterCodes();
        // 获取当前用户所在工作区
        RuleEngineFormula ruleEngineFormula = ruleEngineFormulaManager.getOne(new LambdaQueryWrapper<RuleEngineFormula>()
                .eq(!StringUtils.isEmpty(updateFormulaRequest.getName()), RuleEngineFormula::getName, updateFormulaRequest.getName())
        );
        if (ruleEngineFormula == null || ruleEngineFormula.getId().equals(updateFormulaRequest.getId())) {
            this.validFormulaParam(inputParameterCodes);
            ruleEngineFormula = new RuleEngineFormula();
            ruleEngineFormula.setId(updateFormulaRequest.getId());
            ruleEngineFormula.setName(updateFormulaRequest.getName());
            ruleEngineFormula.setValue(updateFormulaRequest.getValue());
            ruleEngineFormula.setValueType(updateFormulaRequest.getValueType());
            ruleEngineFormula.setDescription(updateFormulaRequest.getDescription());
            ruleEngineFormula.setReferenceParameterCode(JSON.toJSONString(inputParameterCodes));
        } else {
            throw new ValidationException("表达式名称不得重复!");
        }
        return this.ruleEngineFormulaManager.updateById(ruleEngineFormula);
    }

    /**
     * 校验表达式里面的参数是否存在
     *
     * @param inputParameterCodes in
     */
    private void validFormulaParam(Set<String> inputParameterCodes) {
        if (CollUtil.isEmpty(inputParameterCodes)) {
            return;
        }
        Integer count = this.ruleEngineInputParameterManager.lambdaQuery()
                .in(RuleEngineInputParameter::getCode, inputParameterCodes).count();
        if (count != inputParameterCodes.size()) {
            throw new ApiException("参数存在缺失，请检查是否存在以下参数：" + inputParameterCodes);
        }
    }

    /**
     * 根据id获取表达式
     *
     * @param id param
     * @return r
     */
    @Override
    public GetFormulaResponse getFormula(Integer id) {
        RuleEngineFormula ruleEngineFormula = ruleEngineFormulaManager.getById(id);
        if (ruleEngineFormula == null) {
            throw new ValidationException("表达式不存在!");
        }
        GetFormulaResponse getFormulaResponse = new GetFormulaResponse();
        BeanUtils.copyProperties(ruleEngineFormula, getFormulaResponse);
        return getFormulaResponse;
    }

    /**
     * 删除表达式
     *
     * @param id param
     * @return r
     */
    @Override
    public Boolean deleteFormula(Integer id) {
        // 校验是否有人使用
        this.dataReferenceService.validDataReference(VariableType.FORMULA.getType(), id);
        return this.ruleEngineFormulaManager.removeById(id);
    }

    /**
     * 验证表达式名称是否存在
     *
     * @param validationExpressionNameRequest v
     * @return r
     */
    @Override
    public Boolean validationExpressionName(ValidationExpressionNameRequest validationExpressionNameRequest) {
        Workspace workspace = Context.getCurrentWorkspace();
        return this.verifyName(validationExpressionNameRequest.getName(), workspace.getId());
    }


}
