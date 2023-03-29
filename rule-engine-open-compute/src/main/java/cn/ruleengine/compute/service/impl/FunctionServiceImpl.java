package cn.ruleengine.compute.service.impl;

import cn.ruleengine.compute.enums.ErrorCodeEnum;
import cn.ruleengine.compute.exception.ApiException;
import cn.ruleengine.compute.service.FunctionService;
import cn.ruleengine.compute.store.entity.RuleEngineFunction;
import cn.ruleengine.compute.store.manager.RuleEngineFunctionManager;
import cn.ruleengine.compute.vo.ExecuteFunctionRequest;
import cn.ruleengine.compute.vo.ParamValue;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.value.Constant;
import cn.ruleengine.core.value.Function;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.core.value.ValueType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ApplicationContext applicationContext;

    /**
     * 函数模拟测试
     *
     * @param executeTestRequest 函数入参值
     * @return result
     */
    @Override
    public Object run(ExecuteFunctionRequest executeTestRequest) {
        Integer functionId = executeTestRequest.getId();
        RuleEngineFunction engineFunction = this.ruleEngineFunctionManager.getById(functionId);
        if (engineFunction == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(),"不存在函数：{}", functionId);
        }
        String executor = engineFunction.getExecutor();
        if (this.applicationContext.containsBean(executor)) {
            Object abstractFunction = this.applicationContext.getBean(executor);
            // 函数测试均为固定值
            List<ParamValue> paramValues = executeTestRequest.getParamValues();
            Map<String, Value> param = new HashMap<>(paramValues.size());
            for (ParamValue paramValue : paramValues) {
                Constant constant = new Constant(paramValue.getValue(), ValueType.getByValue(paramValue.getValueType()));
                param.put(paramValue.getCode(), constant);
            }
            Function function = new Function(functionId, abstractFunction, ValueType.STRING, param);
            // 无规则参数 input==null
            return function.getValue(null, new RuleEngineConfiguration());
        } else {
            throw new ApiException("容器中找不到{}函数", executor);
        }
    }

}
