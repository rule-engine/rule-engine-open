package cn.ruleengine.web.service;


import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.vo.variable.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface VariableService {

    /**
     * 添加变量
     *
     * @param addConditionRequest 变量信息
     * @return true
     */
    Boolean add(AddVariableRequest addConditionRequest);

    /**
     * 变量名称是否存在
     *
     * @param verifyVariableNameRequest 变量
     * @return true存在
     */
    Boolean varNameIsExists(VerifyVariableNameRequest verifyVariableNameRequest);

    /**
     * 变量列表
     *
     * @param pageRequest param
     * @return result
     */
    PageResult<ListVariableResponse> list(PageRequest<ListVariableRequest> pageRequest);

    /**
     * 根据id查询变量
     *
     * @param id 变量id
     * @return var
     */
    GetVariableResponse get(Integer id);

    /**
     * 根据id更新变量
     *
     * @param updateVariableRequest param
     * @return true
     */
    Boolean update(UpdateVariableRequest updateVariableRequest);

    /**
     * 根据id删除变量
     *
     * @param id 变量id
     * @return true
     */
    Boolean delete(Integer id);
}
