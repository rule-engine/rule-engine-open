package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.common.vo.Rows;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.ConditionService;
import cn.ruleengine.web.service.DataReferenceService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.RuleEngineCondition;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroupCondition;
import cn.ruleengine.web.store.entity.RuleEngineInputParameter;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineInputParameterManager;
import cn.ruleengine.web.store.manager.RuleEngineVariableManager;
import cn.ruleengine.web.store.mapper.RuleEngineConditionMapper;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Component
public class ConditionServiceImpl implements ConditionService {

    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineConditionMapper ruleEngineConditionMapper;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineInputParameterManager ruleEngineInputParameterManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private DataReferenceService dataReferenceService;


    /**
     * 保存条件
     *
     * @param addConditionRequest 条件配置信息
     * @return true
     */
    @Override
    public Integer save(AddConditionRequest addConditionRequest) {
        Workspace workspace = Context.getCurrentWorkspace();
        RuleEngineCondition condition = new RuleEngineCondition();
        UserData userData = Context.getCurrentUser();
        condition.setCreateUserId(userData.getId());
        condition.setCreateUserName(userData.getUsername());
        condition.setName(addConditionRequest.getName());
        condition.setDescription(addConditionRequest.getDescription());
        // 条件配置信息
        this.configBeanCopyToCondition(condition, addConditionRequest.getConfig());
        condition.setWorkspaceId(workspace.getId());
        ruleEngineConditionManager.save(condition);
        return condition.getId();
    }

    /**
     * 条件名称是否存在
     *
     * @param name 条件名称
     * @return true存在
     */
    @Override
    public Boolean conditionNameIsExists(String name) {
        Workspace workspace = Context.getCurrentWorkspace();
        return this.ruleEngineConditionManager.lambdaQuery()
                .eq(RuleEngineCondition::getWorkspaceId, workspace.getId())
                .eq(RuleEngineCondition::getName, name)
                .exists();
    }

    /**
     * 根绝id查询条件信息
     *
     * @param id 条件id
     * @return ConditionResponse
     */
    @Override
    public ConditionBody getById(Integer id) {
        RuleEngineCondition condition = this.ruleEngineConditionManager.lambdaQuery()
                .eq(RuleEngineCondition::getId, id)
                .one();
        if (condition == null) {
            throw new ApiException("根据Id:{},没有查询到数据", id);
        }
        return getConditionResponse(condition);
    }

    /**
     * 条件转换
     *
     * @param engineCondition engineCondition
     * @return ConditionResponse
     */
    @Override
    public ConditionBody getConditionResponse(RuleEngineCondition engineCondition) {
        ConditionBody conditionResponse = new ConditionBody();

        conditionResponse.setDescription(engineCondition.getDescription());
        conditionResponse.setId(engineCondition.getId());
        conditionResponse.setName(engineCondition.getName());

        ConfigBean configBean = getConfigBean(engineCondition);
        conditionResponse.setConfig(configBean);
        return conditionResponse;
    }

    /**
     * 条件转换
     *
     * @param engineCondition             engineCondition
     * @param variableMap                 条件用到的变量
     * @param ruleEngineInputParameterMap 条件用到的规则参数
     * @return ConditionResponse
     */
    @Override
    public ConditionBody getConditionResponse(RuleEngineCondition engineCondition, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineInputParameter> ruleEngineInputParameterMap) {
        ConditionBody conditionResponse = new ConditionBody();

        conditionResponse.setDescription(engineCondition.getDescription());
        conditionResponse.setId(engineCondition.getId());
        conditionResponse.setName(engineCondition.getName());

        ConfigBean configBean = this.getConfigBean(engineCondition, variableMap, ruleEngineInputParameterMap);
        conditionResponse.setConfig(configBean);
        return conditionResponse;
    }

    /**
     * 条件配置信息
     *
     * @param engineCondition 条件
     * @return ConfigBean
     */
    public ConfigBean getConfigBean(RuleEngineCondition engineCondition) {
        ConfigBean configBean = new ConfigBean();

        ConfigValue leftValue = valueResolve.getConfigValue(engineCondition.getLeftValue(), engineCondition.getLeftType(), engineCondition.getLeftValueType());
        configBean.setLeftValue(leftValue);

        configBean.setSymbol(engineCondition.getSymbol());

        ConfigValue rightValue = valueResolve.getConfigValue(engineCondition.getRightValue(), engineCondition.getRightType(), engineCondition.getRightValueType());
        configBean.setRightValue(rightValue);
        return configBean;
    }


    /**
     * 条件列表
     *
     * @param pageRequest 分页查询信息
     * @return page
     */
    @Override
    public PageResult<ListConditionResponse> list(PageRequest<ListConditionRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        QueryWrapper<RuleEngineCondition> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RuleEngineCondition::getWorkspaceId, workspace.getId());
        // 查询的数据排序
        PageUtils.defaultOrder(orders, wrapper);
        ListConditionRequest query = pageRequest.getQuery();
        if (Validator.isNotEmpty(query.getName())) {
            wrapper.lambda().like(RuleEngineCondition::getName, query.getName());
        }
        IPage<RuleEngineCondition> iPage = this.ruleEngineConditionManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), wrapper);
        List<RuleEngineCondition> engineConditions = iPage.getRecords();
        PageResult<ListConditionResponse> pageResult = new PageResult<>();
        if (CollUtil.isEmpty(engineConditions)) {
            return pageResult;
        }
        Map<Integer, RuleEngineInputParameter> inputParameterMap = this.getConditionInputParameterMap(engineConditions);
        Map<Integer, RuleEngineVariable> variableMap = this.getConditionVariableMap(engineConditions);
        // 类型转换处理
        List<ListConditionResponse> conditionResponses = engineConditions.stream().map(m -> {
            ListConditionResponse listConditionResponse = new ListConditionResponse();
            listConditionResponse.setId(m.getId());
            listConditionResponse.setName(m.getName());
            listConditionResponse.setDescription(m.getDescription());
            listConditionResponse.setCreateTime(m.getCreateTime());
            ConfigBean configBean = this.getConfigBean(m, variableMap, inputParameterMap);
            listConditionResponse.setConfig(configBean);
            // 获取符号说明
            String symbol = Operator.getByName(m.getSymbol()).getExplanation();
            listConditionResponse.setConfigInfo(String.format("%s %s %s", configBean.getLeftValue().getValueName(), symbol, configBean.getRightValue().getValueName()));
            return listConditionResponse;
        }).collect(Collectors.toList());
        pageResult.setData(new Rows<>(conditionResponses, PageUtils.getPageResponse(iPage)));
        return pageResult;
    }

    /**
     * 条件配置信息
     *
     * @param engineCondition   条件
     * @param variableMap       条件引用的变量
     * @param inputParameterMap 条件引用的规则参数
     * @return ConfigBean
     */
    public ConfigBean getConfigBean(RuleEngineCondition engineCondition, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineInputParameter> inputParameterMap) {
        ConfigBean configBean = new ConfigBean();

        ConfigValue leftValue = this.valueResolve.getConfigValue(engineCondition.getLeftValue(), engineCondition.getLeftType(), engineCondition.getLeftValueType(), variableMap, inputParameterMap);
        configBean.setLeftValue(leftValue);

        configBean.setSymbol(engineCondition.getSymbol());

        ConfigValue rightValue = this.valueResolve.getConfigValue(engineCondition.getRightValue(), engineCondition.getRightType(), engineCondition.getRightValueType(), variableMap, inputParameterMap);
        configBean.setRightValue(rightValue);
        return configBean;
    }

    /**
     * 获取条件中的变量
     *
     * @param ruleEngineConditions 条件信息
     * @return map
     */
    @Override
    public Map<Integer, RuleEngineVariable> getConditionVariableMap(Collection<RuleEngineCondition> ruleEngineConditions) {
        // 获取条件中的所有的变量数据
        Set<String> variableIds = new HashSet<>();
        for (RuleEngineCondition engineCondition : ruleEngineConditions) {
            Integer rightType = engineCondition.getRightType();
            if (rightType.equals(VariableType.VARIABLE.getType())) {
                variableIds.add(engineCondition.getRightValue());
            }
            Integer leftType = engineCondition.getLeftType();
            if (leftType.equals(VariableType.VARIABLE.getType())) {
                variableIds.add(engineCondition.getLeftValue());
            }
        }
        return Optional.of(variableIds).filter(CollUtil::isNotEmpty)
                .map(m -> ruleEngineVariableManager.lambdaQuery().in(RuleEngineVariable::getId, m).list()
                        .stream().collect(Collectors.toMap(RuleEngineVariable::getId, v -> v)))
                .orElse(new HashMap<>());
    }

    /**
     * 获取条件中的规则参数
     *
     * @param ruleEngineConditions 条件信息
     * @return map
     */
    @Override
    public Map<Integer, RuleEngineInputParameter> getConditionInputParameterMap(Collection<RuleEngineCondition> ruleEngineConditions) {
        Set<String> inputParamIds = new HashSet<>();
        for (RuleEngineCondition engineCondition : ruleEngineConditions) {
            Integer rightType = engineCondition.getRightType();
            if (rightType.equals(VariableType.INPUT_PARAMETER.getType())) {
                inputParamIds.add(engineCondition.getRightValue());
            }
            Integer leftType = engineCondition.getLeftType();
            if (leftType.equals(VariableType.INPUT_PARAMETER.getType())) {
                inputParamIds.add(engineCondition.getLeftValue());
            }
        }
        return Optional.of(inputParamIds).filter(CollUtil::isNotEmpty)
                .map(m -> ruleEngineInputParameterManager.lambdaQuery().in(RuleEngineInputParameter::getId, m).list()
                        .stream().collect(Collectors.toMap(RuleEngineInputParameter::getId, v -> v)))
                .orElse(new HashMap<>());
    }


    /**
     * 更新条件
     *
     * @param updateConditionRequest 更新条件
     * @return true
     */
    @Override
    public Boolean update(UpdateConditionRequest updateConditionRequest) {
        RuleEngineCondition ruleEngineCondition = this.ruleEngineConditionManager.lambdaQuery()
                .eq(RuleEngineCondition::getId, updateConditionRequest.getId())
                .one();
        if (ruleEngineCondition == null) {
            throw new ApiException("规则条件找不到：{}", updateConditionRequest.getId());
        }
        if (!ruleEngineCondition.getName().equals(updateConditionRequest.getName())) {
            if (this.conditionNameIsExists(updateConditionRequest.getName())) {
                throw new ApiException("条件名称：{}已经存在", updateConditionRequest.getName());
            }
        }
        Integer conditionId = updateConditionRequest.getId();
        // 更新条件
        RuleEngineCondition condition = new RuleEngineCondition();
        condition.setId(conditionId);
        condition.setName(updateConditionRequest.getName());
        condition.setDescription(updateConditionRequest.getDescription());
        // 条件配置信息
        this.configBeanCopyToCondition(condition, updateConditionRequest.getConfig());
        this.ruleEngineConditionManager.updateById(condition);
        return true;
    }

    /**
     * 从ConfigBean信息复制到RuleEngineCondition
     *
     * @param condition RuleEngineCondition
     * @param config    请求的条件配置数据
     */
    private void configBeanCopyToCondition(RuleEngineCondition condition, ConfigBean config) {
        ConfigValue leftValue = config.getLeftValue();
        condition.setLeftValueType(leftValue.getValueType());
        condition.setLeftType(leftValue.getType());
        condition.setLeftValue(leftValue.getValue());
        String symbol = config.getSymbol();
        condition.setSymbol(symbol);
        ConfigValue rightValue = config.getRightValue();
        condition.setRightValueType(rightValue.getValueType());
        condition.setRightType(rightValue.getType());
        condition.setRightValue(rightValue.getValue());
    }

    /**
     * 删除条件
     *
     * @param id 条件id
     * @return true：删除成功
     */
    @Override
    public Boolean delete(Integer id) {
        boolean exists = ruleEngineConditionGroupConditionManager.lambdaQuery()
                .eq(RuleEngineConditionGroupCondition::getConditionId, id)
                .exists();
        if (exists) {
            throw new ApiException("有规则/规则集在引用此条件，无法删除");
        }
        return this.ruleEngineConditionManager.removeById(id);
    }


}
