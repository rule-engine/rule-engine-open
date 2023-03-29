package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.common.vo.Rows;
import cn.ruleengine.core.value.Formula;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.listener.body.VariableMessageBody;
import cn.ruleengine.web.listener.event.VariableEvent;
import cn.ruleengine.web.service.DataReferenceService;
import cn.ruleengine.web.service.VariableService;
import cn.ruleengine.web.store.entity.RuleEngineFunction;
import cn.ruleengine.web.store.entity.RuleEngineFunctionValue;
import cn.ruleengine.web.store.entity.RuleEngineInputParameter;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.store.manager.RuleEngineFunctionManager;
import cn.ruleengine.web.store.manager.RuleEngineFunctionValueManager;
import cn.ruleengine.web.store.manager.RuleEngineInputParameterManager;
import cn.ruleengine.web.store.manager.RuleEngineVariableManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.variable.*;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class VariableServiceImpl implements VariableService {

    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;
    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private RuleEngineInputParameterManager ruleEngineInputParameterManager;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private DataReferenceService dataReferenceService;

    /**
     * 添加变量
     *
     * @param addConditionRequest 变量信息
     * @return true
     */
    @Override
    public Boolean add(AddVariableRequest addConditionRequest) {
        VerifyVariableNameRequest verifyVariableNameRequest = new VerifyVariableNameRequest();
        BeanUtils.copyProperties(addConditionRequest, verifyVariableNameRequest);
        if (this.varNameIsExists(verifyVariableNameRequest)) {
            throw new ApiException("变量名称：{}已经存在", addConditionRequest.getName());
        }
        if (addConditionRequest.getType().equals(VariableType.FORMULA.getType())) {
            // 校验表达式合法性
            Formula.ExpressionProcessor expressionProcessor = new Formula.ExpressionProcessor(addConditionRequest.getValue());
            this.validFormulaParam(expressionProcessor.getInputParameterCodes());
        }
        RuleEngineVariable engineVariable = new RuleEngineVariable();
        UserData userData = Context.getCurrentUser();
        engineVariable.setCreateUserId(userData.getId());
        engineVariable.setCreateUserName(userData.getUsername());
        engineVariable.setName(addConditionRequest.getName());
        engineVariable.setDescription(addConditionRequest.getDescription());
        engineVariable.setValueType(addConditionRequest.getValueType());
        engineVariable.setValue(addConditionRequest.getValue());
        engineVariable.setType(addConditionRequest.getType());
        engineVariable.setDataId(addConditionRequest.getDataId());
        engineVariable.setDataType(addConditionRequest.getDataType());
        Workspace workspace = Context.getCurrentWorkspace();
        engineVariable.setWorkspaceId(workspace.getId());
        this.ruleEngineVariableManager.save(engineVariable);
        if (addConditionRequest.getType().equals(VariableType.FUNCTION.getType())) {
            // 保存函数参数值
            List<ParamValue> paramValues = addConditionRequest.getFunction().getParamValues();
            RuleEngineFunction ruleEngineFunction = this.getFunction(engineVariable.getValue());
            this.saveFunctionParamValues(ruleEngineFunction.getId(), engineVariable, paramValues);
        }
        // 通知加载变量
        VariableMessageBody variableMessageBody = new VariableMessageBody();
        variableMessageBody.setType(VariableMessageBody.Type.LOAD);
        variableMessageBody.setId(engineVariable.getId());
        this.eventPublisher.publishEvent(new VariableEvent(variableMessageBody));
        return true;
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
                .eq(RuleEngineInputParameter::getWorkspaceId, Context.getCurrentWorkspace().getId())
                .in(RuleEngineInputParameter::getCode, inputParameterCodes)
                .count();
        if (count != inputParameterCodes.size()) {
            throw new ApiException("参数存在缺失，请检查是否存在以下参数：" + inputParameterCodes);
        }
    }


    /**
     * 变量名称是否存在
     *
     * @param verifyVariableNameRequest 变量名称
     * @return true存在
     */
    @Override
    public Boolean varNameIsExists(VerifyVariableNameRequest verifyVariableNameRequest) {
        Workspace workspace = Context.getCurrentWorkspace();
        return this.ruleEngineVariableManager.lambdaQuery()
                .eq(RuleEngineVariable::getWorkspaceId, workspace.getId())
                .eq(RuleEngineVariable::getName, verifyVariableNameRequest.getName()).exists();
    }

    /**
     * 保存函数参数值
     *
     * @param functionId     函数id
     * @param engineVariable 变量
     * @param paramValues    函数参数值
     */
    public void saveFunctionParamValues(Integer functionId, RuleEngineVariable engineVariable, List<ParamValue> paramValues) {
        List<RuleEngineFunctionValue> engineFunctionValues = paramValues.stream().map(m -> {
            // 变量自己不能引用自己
            if (Objects.equals(engineVariable.getName(), m.getName())) {
                throw new ApiException("变量不可以引用自身");
            }
            RuleEngineFunctionValue engineFunctionValue = new RuleEngineFunctionValue();
            engineFunctionValue.setFunctionId(functionId);
            engineFunctionValue.setParamCode(m.getCode());
            engineFunctionValue.setParamName(m.getName());
            engineFunctionValue.setVariableId(engineVariable.getId());
            engineFunctionValue.setType(m.getType());
            engineFunctionValue.setValueType(m.getValueType());
            engineFunctionValue.setValue(m.getValue());
            return engineFunctionValue;
        }).collect(Collectors.toList());
        this.ruleEngineFunctionValueManager.saveBatch(engineFunctionValues);
    }

    /**
     * 变量列表
     *
     * @param pageRequest param
     * @return result
     */
    @Override
    public PageResult<ListVariableResponse> list(PageRequest<ListVariableRequest> pageRequest) {
        ListVariableRequest query = pageRequest.getQuery();
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        QueryWrapper<RuleEngineVariable> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RuleEngineVariable::getWorkspaceId, workspace.getId());
        PageUtils.defaultOrder(orders, wrapper);
        wrapper.lambda().eq(Validator.isNotEmpty(query.getDataType()), RuleEngineVariable::getDataType, query.getDataType());
        wrapper.lambda().eq(Validator.isNotEmpty(query.getDataId()), RuleEngineVariable::getDataId, query.getDataId());
        if (CollUtil.isNotEmpty(query.getValueType())) {
            wrapper.lambda().in(RuleEngineVariable::getValueType, query.getValueType());
        }
        if (Validator.isNotEmpty(query.getName())) {
            wrapper.lambda().like(RuleEngineVariable::getName, query.getName());
        }
        Page<RuleEngineVariable> variablePage = ruleEngineVariableManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), wrapper);
        List<RuleEngineVariable> records = variablePage.getRecords();
        PageResult<ListVariableResponse> pageResult = new PageResult<>();
        if (CollUtil.isEmpty(records)) {
            return pageResult;
        }
        // 获取用到的所有的函数信息
        Set<String> functionIds = records.stream().filter(f -> Objects.equals(f.getType(), VariableType.FUNCTION.getType()))
                .map(RuleEngineVariable::getValue)
                .collect(Collectors.toSet());
        Map<Integer, String> functionMap;
        if (CollUtil.isNotEmpty(functionIds)) {
            List<RuleEngineFunction> list = this.ruleEngineFunctionManager.lambdaQuery().in(RuleEngineFunction::getId, functionIds)
                    .list();
            functionMap = list.stream().collect(Collectors.toMap(RuleEngineFunction::getId, RuleEngineFunction::getName));
        } else {
            functionMap = Collections.emptyMap();
        }
        List<ListVariableResponse> collect = records.stream().map(m -> {
            ListVariableResponse listVariableResponse = new ListVariableResponse();
            listVariableResponse.setId(m.getId());
            listVariableResponse.setName(m.getName());
            listVariableResponse.setValue(m.getValue());
            listVariableResponse.setType(m.getType());
            if (m.getType().equals(VariableType.FUNCTION.getType())) {
                listVariableResponse.setFunctionName(functionMap.get(Integer.valueOf(m.getValue())));
            }
            listVariableResponse.setValueType(m.getValueType());
            listVariableResponse.setDescription(m.getDescription());
            listVariableResponse.setCreateTime(m.getCreateTime());
            return listVariableResponse;
        }).collect(Collectors.toList());

        pageResult.setData(new Rows<>(collect, PageUtils.getPageResponse(variablePage)));
        return pageResult;
    }

    /**
     * 变量分为固定值变量,函数变量
     *
     * @param id 变量id
     * @return var
     */
    @Override
    public GetVariableResponse get(Integer id) {
        RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.lambdaQuery()
                .eq(RuleEngineVariable::getId, id)
                .one();
        if (ruleEngineVariable == null) {
            return null;
        }
        GetVariableResponse variableResponse = BasicConversion.INSTANCE.convert(ruleEngineVariable);
        if (ruleEngineVariable.getType().equals(VariableType.CONSTANT.getType())) {
            return variableResponse;
        } else if (ruleEngineVariable.getType().equals(VariableType.FUNCTION.getType())) {
            String functionId = ruleEngineVariable.getValue();
            RuleEngineFunction engineFunction = ruleEngineFunctionManager.getById(functionId);
            VariableFunction function = new VariableFunction();
            function.setId(engineFunction.getId());
            function.setName(engineFunction.getName());
            function.setReturnValueType(engineFunction.getReturnValueType());
            // 处理函数入参值
            List<RuleEngineFunctionValue> functionValues = ruleEngineFunctionValueManager.lambdaQuery()
                    .eq(RuleEngineFunctionValue::getVariableId, id)
                    .eq(RuleEngineFunctionValue::getFunctionId, functionId).list();
            List<ParamValue> paramValueList = functionValues.stream().map(m -> {
                ParamValue paramValue = new ParamValue();
                paramValue.setName(m.getParamName());
                paramValue.setCode(m.getParamCode());
                paramValue.setType(m.getType());
                paramValue.setValue(m.getValue());
                paramValue.setValueType(m.getValueType());
                String valueName = m.getValue();
                if (VariableType.INPUT_PARAMETER.getType().equals(m.getType())) {
                    valueName = ruleEngineInputParameterManager.getById(m.getValue()).getName();
                } else if (VariableType.VARIABLE.getType().equals(m.getType())) {
                    RuleEngineVariable engineVariable = ruleEngineVariableManager.getById(m.getValue());
                    valueName = engineVariable.getName();
                }
                paramValue.setValueName(valueName);
                return paramValue;
            }).collect(Collectors.toList());
            function.setParamValues(paramValueList);
            variableResponse.setFunction(function);
        }
        return variableResponse;
    }

    /**
     * 根据id更新变量
     *
     * @param updateVariableRequest param
     * @return true
     */
    @Override
    public Boolean update(UpdateVariableRequest updateVariableRequest) {
        RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.lambdaQuery()
                .eq(RuleEngineVariable::getId, updateVariableRequest.getId())
                .one();
        if (ruleEngineVariable == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到更新的变量：{}", updateVariableRequest.getId());
        }
        if (updateVariableRequest.getType().equals(VariableType.FORMULA.getType())) {
            // 校验表达式合法性
            Formula.ExpressionProcessor expressionProcessor = new Formula.ExpressionProcessor(updateVariableRequest.getValue());
            this.validFormulaParam(expressionProcessor.getInputParameterCodes());
        }
        VerifyVariableNameRequest verifyVariableNameRequest = new VerifyVariableNameRequest();
        verifyVariableNameRequest.setDataId(ruleEngineVariable.getDataId());
        verifyVariableNameRequest.setDataType(ruleEngineVariable.getDataType());
        verifyVariableNameRequest.setName(updateVariableRequest.getName());
        if (!updateVariableRequest.getName().equals(ruleEngineVariable.getName())) {
            if (this.varNameIsExists(verifyVariableNameRequest)) {
                throw new ApiException("变量名称：{}已经存在", updateVariableRequest.getName());
            }
        }
        RuleEngineVariable engineVariable = new RuleEngineVariable();
        engineVariable.setId(updateVariableRequest.getId());
        engineVariable.setName(updateVariableRequest.getName());
        engineVariable.setDescription(updateVariableRequest.getDescription());
        engineVariable.setValue(updateVariableRequest.getValue());
        engineVariable.setType(updateVariableRequest.getType());
        this.ruleEngineVariableManager.updateById(engineVariable);
        // 函数信息
        if (updateVariableRequest.getType().equals(VariableType.FUNCTION.getType())) {
            // 删除原有的函数值信息
            this.ruleEngineFunctionValueManager.lambdaUpdate()
                    .eq(RuleEngineFunctionValue::getFunctionId, updateVariableRequest.getValue())
                    .eq(RuleEngineFunctionValue::getVariableId, engineVariable.getId())
                    .remove();
            RuleEngineFunction ruleEngineFunction = this.getFunction(engineVariable.getValue());
            // 保存函数参数值
            List<ParamValue> paramValues = updateVariableRequest.getFunction().getParamValues();
            this.saveFunctionParamValues(ruleEngineFunction.getId(), engineVariable, paramValues);
        }
        // 通知更新变量
        VariableMessageBody variableMessageBody = new VariableMessageBody();
        variableMessageBody.setType(VariableMessageBody.Type.UPDATE);
        variableMessageBody.setId(engineVariable.getId());
        this.eventPublisher.publishEvent(new VariableEvent(variableMessageBody));
        return true;
    }

    /**
     * 获取当前函数并校验是否存在
     *
     * @param functionId 函数id
     * @return r
     */
    public RuleEngineFunction getFunction(String functionId) {
        RuleEngineFunction engineFunction = this.ruleEngineFunctionManager.getById(functionId);
        if (engineFunction == null) {
            throw new ApiException("函数不存在");
        }
        return engineFunction;
    }

    /**
     * 根据id删除变量
     *
     * @param id 变量id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        // 校验是否有人使用
        this.dataReferenceService.validDataReference(VariableType.VARIABLE.getType(), id);
        // 删除
        this.ruleEngineVariableManager.removeById(id);
        // 通知移除变量
        VariableMessageBody variableMessageBody = new VariableMessageBody();
        variableMessageBody.setType(VariableMessageBody.Type.REMOVE);
        variableMessageBody.setId(id);
        this.eventPublisher.publishEvent(new VariableEvent(variableMessageBody));
        return true;
    }

}
