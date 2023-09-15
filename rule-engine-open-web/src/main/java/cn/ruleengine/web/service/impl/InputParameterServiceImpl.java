package cn.ruleengine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.DataReferenceService;
import cn.ruleengine.web.service.InputParameterService;
import cn.ruleengine.web.store.entity.RuleEngineInputParameter;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.store.manager.RuleEngineInputParameterManager;
import cn.ruleengine.web.store.manager.RuleEngineVariableManager;
import cn.ruleengine.web.store.mapper.RuleEngineFormulaMapper;
import cn.ruleengine.web.util.OrikaBeanMapper;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.parameter.*;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Service
public class InputParameterServiceImpl implements InputParameterService {

    @Resource
    private RuleEngineInputParameterManager ruleEngineInputParameterManager;
    @Resource
    private DataReferenceService dataReferenceService;
    @Resource
    private RuleEngineFormulaMapper ruleEngineFormulaMapper;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;

    /**
     * 添加规则参数
     *
     * @param addConditionRequest 规则参数信息
     * @return true
     */
    @Override
    public Boolean add(AddInputParameterRequest addConditionRequest) {
        if (addConditionRequest.getCode().startsWith(Input.SYSTEM_KEY_PREFIX)) {
            throw new ApiException("参数不能以{}开头", Input.SYSTEM_KEY_PREFIX);
        }
        VerifyInputParameterCodeRequest verifyInputParameterCodeRequest = new VerifyInputParameterCodeRequest();
        BeanUtils.copyProperties(addConditionRequest, verifyInputParameterCodeRequest);
        if (this.codeIsExists(verifyInputParameterCodeRequest)) {
            throw new ApiException("规则参数Code：{}已经存在", addConditionRequest.getCode());
        }
        Workspace workspace = Context.getCurrentWorkspace();
        RuleEngineInputParameter inputParameter = new RuleEngineInputParameter();
        UserData userData = Context.getCurrentUser();
        inputParameter.setCreateUserId(userData.getId());
        inputParameter.setCreateUserName(userData.getUsername());
        inputParameter.setName(addConditionRequest.getName());
        inputParameter.setCode(addConditionRequest.getCode());
        inputParameter.setWorkspaceId(workspace.getId());
        inputParameter.setDescription(addConditionRequest.getDescription());
        inputParameter.setValueType(addConditionRequest.getValueType());
        return ruleEngineInputParameterManager.save(inputParameter);
    }

    /**
     * 规则参数code是否存在
     *
     * @param verifyInputParameterCodeRequest 规则参数code
     * @return true存在
     */
    @Override
    public Boolean codeIsExists(VerifyInputParameterCodeRequest verifyInputParameterCodeRequest) {
        Workspace workspace = Context.getCurrentWorkspace();
        return this.ruleEngineInputParameterManager.lambdaQuery()
                .eq(RuleEngineInputParameter::getWorkspaceId, workspace.getId())
                .eq(RuleEngineInputParameter::getCode, verifyInputParameterCodeRequest.getCode())
                .exists();
    }

    /**
     * 规则参数列表
     *
     * @param pageRequest param
     * @return r
     */
    @Override
    public PageResult<ListInputParameterResponse> list(PageRequest<ListInputParameterRequest> pageRequest) {
        ListInputParameterRequest query = pageRequest.getQuery();
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(ruleEngineInputParameterManager, page, () -> {
            QueryWrapper<RuleEngineInputParameter> wrapper = new QueryWrapper<>();
            PageUtils.defaultOrder(orders, wrapper);
            wrapper.lambda().eq(RuleEngineInputParameter::getWorkspaceId, workspace.getId());
            if (CollUtil.isNotEmpty(query.getValueType())) {
                wrapper.lambda().in(RuleEngineInputParameter::getValueType, query.getValueType());
            }
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineInputParameter::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineInputParameter::getCode, query.getCode());
            }
            return wrapper;
        }, m -> {
            ListInputParameterResponse inputParameterResponse = new ListInputParameterResponse();
            BeanUtil.copyProperties(m, inputParameterResponse);
            return inputParameterResponse;
        });
    }

    /**
     * 根据id查询规则参数
     *
     * @param id 规则参数id
     * @return r
     */
    @Override
    public GetInputParameterResponse get(Integer id) {
        RuleEngineInputParameter inputParameter = this.ruleEngineInputParameterManager.lambdaQuery()
                .eq(RuleEngineInputParameter::getId, id)
                .one();
        return OrikaBeanMapper.map(inputParameter, GetInputParameterResponse.class);
    }

    /**
     * 根据规则参数id更新规则参数
     *
     * @param inputParameterRequest 规则参数信息
     * @return true
     */
    @Override
    public Boolean update(UpdateInputParameterRequest inputParameterRequest) {
        RuleEngineInputParameter inputParameter = this.ruleEngineInputParameterManager.lambdaQuery()
                .eq(RuleEngineInputParameter::getId, inputParameterRequest.getId())
                .one();
        if (inputParameter == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到更新的规则参数");
        }
        inputParameter.setId(inputParameterRequest.getId());
        inputParameter.setName(inputParameterRequest.getName());
        inputParameter.setDescription(inputParameterRequest.getDescription());
        return this.ruleEngineInputParameterManager.updateById(inputParameter);
    }

    /**
     * 根据id删除规则参数
     * <p>
     * 发布规则或者决策表规则参数即使被删除也不影响引擎加载
     *
     * @param id 规则参数id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        RuleEngineInputParameter inputParameter = ruleEngineInputParameterManager.getById(id);
        if (inputParameter == null) {
            return false;
        }
        // 校验是否有人使用
        this.dataReferenceService.validDataReference(VariableType.INPUT_PARAMETER.getType(), id);
        String code = inputParameter.getCode();
        if (this.ruleEngineVariableManager.lambdaQuery().eq(RuleEngineVariable::getType, VariableType.FORMULA.getType())
                .like(RuleEngineVariable::getValue, "#".concat(code))
                .exists()) {
            throw new ApiException("有表达式变量在引用，无法删除");
        }
        // 删除
        return ruleEngineInputParameterManager.removeById(id);
    }

}
