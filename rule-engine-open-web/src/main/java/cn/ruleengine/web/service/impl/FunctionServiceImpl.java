package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.common.vo.Rows;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.FunctionService;
import cn.ruleengine.web.store.entity.RuleEngineFunction;
import cn.ruleengine.web.store.entity.RuleEngineFunctionParam;
import cn.ruleengine.web.store.manager.RuleEngineFunctionManager;
import cn.ruleengine.web.store.manager.RuleEngineFunctionParamManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.function.FunctionParam;
import cn.ruleengine.web.vo.function.GetFunctionResponse;
import cn.ruleengine.web.vo.function.ListFunctionRequest;
import cn.ruleengine.web.vo.function.ListFunctionResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Service
public class FunctionServiceImpl implements FunctionService {

    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private RuleEngineFunctionParamManager ruleEngineFunctionParamManager;
    @Resource
    private ApplicationContext applicationContext;

    private static final String FUNCTION_PACKAGE = "cn.ruleengine.web.function";

    /**
     * 函数列表
     *
     * @param pageRequest param
     * @return list
     */
    @Override
    public PageResult<ListFunctionResponse> list(PageRequest<ListFunctionRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();

        QueryWrapper<RuleEngineFunction> wrapper = new QueryWrapper<>();
        PageUtils.defaultOrder(orders, wrapper);
        ListFunctionRequest query = pageRequest.getQuery();
        if (Validator.isNotEmpty(query.getName())) {
            wrapper.lambda().like(RuleEngineFunction::getName, query.getName());
        }
        // 函数返回值
        if (Validator.isNotEmpty(query.getValueType())) {
            wrapper.lambda().eq(RuleEngineFunction::getReturnValueType, query.getValueType());
        }
        IPage<RuleEngineFunction> functionPage = ruleEngineFunctionManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), wrapper);

        List<RuleEngineFunction> records = functionPage.getRecords();

        // 获取本次请求用到的所有函数参数
        Map<Integer, List<RuleEngineFunctionParam>> functionParamMap = Optional.of(records)
                .filter(CollUtil::isNotEmpty)
                .map(m -> {
                    List<Integer> functionIds = m.stream().map(RuleEngineFunction::getId).collect(Collectors.toList());
                    return this.ruleEngineFunctionParamManager.lambdaQuery().in(RuleEngineFunctionParam::getFunctionId, functionIds).list();
                })
                .filter(CollUtil::isNotEmpty)
                .map(m -> m.stream().collect(Collectors.groupingBy(RuleEngineFunctionParam::getFunctionId))).orElse(Collections.emptyMap());

        PageResult<ListFunctionResponse> pageResult = new PageResult<>();
        List<ListFunctionResponse> responseList = records.stream().map(m -> {
            ListFunctionResponse functionResponse = new ListFunctionResponse();
            functionResponse.setId(m.getId());
            functionResponse.setName(m.getName());
            functionResponse.setExecutor(m.getExecutor());
            functionResponse.setReturnValueType(m.getReturnValueType());
            functionResponse.setCreateTime(m.getCreateTime());
            // 处理方法参数
            functionResponse.setParams(this.getFunctionParam(functionParamMap.get(m.getId())));
            return functionResponse;
        }).collect(Collectors.toList());
        pageResult.setData(new Rows<>(responseList, PageUtils.getPageResponse(functionPage)));
        return pageResult;
    }

    /**
     * 查询函数详情
     *
     * @param id 函数id
     * @return 函数信息
     */
    @Override
    public GetFunctionResponse get(Integer id) {
        RuleEngineFunction ruleEngineFunction = this.ruleEngineFunctionManager.getById(id);
        if (ruleEngineFunction == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(),"不存在函数：{}", id);
        }
        GetFunctionResponse functionResponse = new GetFunctionResponse();
        functionResponse.setId(ruleEngineFunction.getId());
        functionResponse.setName(ruleEngineFunction.getName());
        functionResponse.setDescription(ruleEngineFunction.getDescription());
        functionResponse.setExecutor(ruleEngineFunction.getExecutor());
        functionResponse.setReturnValueType(ruleEngineFunction.getReturnValueType());
        // 处理方法参数
        List<RuleEngineFunctionParam> functionParamList = this.ruleEngineFunctionParamManager.lambdaQuery().eq(RuleEngineFunctionParam::getFunctionId, id).list();
        functionResponse.setParams(this.getFunctionParam(functionParamList));
        return functionResponse;
    }


    private void saveFunctionParam(List<FunctionParam> param, Integer functionId) {
        if (CollUtil.isNotEmpty(param)) {
            List<RuleEngineFunctionParam> functionParamList = param.stream().map(m -> {
                RuleEngineFunctionParam ruleEngineFunctionParam = new RuleEngineFunctionParam();
                ruleEngineFunctionParam.setFunctionId(functionId);
                ruleEngineFunctionParam.setParamName(m.getName());
                ruleEngineFunctionParam.setParamCode(m.getCode());
                ruleEngineFunctionParam.setValueType(m.getValueType());
                return ruleEngineFunctionParam;
            }).collect(Collectors.toList());
            this.ruleEngineFunctionParamManager.saveBatch(functionParamList);
        }
    }


    /**
     * 处理函数参数
     *
     * @param functionParamList 函数参数列表
     * @return list
     */
    private List<FunctionParam> getFunctionParam(List<RuleEngineFunctionParam> functionParamList) {
        List<FunctionParam> params = new ArrayList<>();
        if (CollUtil.isNotEmpty(functionParamList)) {
            params = functionParamList.stream().map(m -> {
                FunctionParam param = new FunctionParam();
                param.setName(m.getParamName());
                param.setCode(m.getParamCode());
                param.setValueType(m.getValueType());
                return param;
            }).collect(Collectors.toList());
        }
        return params;
    }
}
