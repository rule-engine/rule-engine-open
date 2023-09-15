package cn.ruleengine.web.service;


import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.vo.parameter.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface InputParameterService {

    /**
     * 添加规则参数
     *
     * @param addConditionRequest 规则参数信息
     * @return true
     */
    Boolean add(AddInputParameterRequest addConditionRequest);

    /**
     * 规则参数code是否存在
     *
     * @param verifyInputParameterCodeRequest 规则参数code
     * @return true存在
     */
    Boolean codeIsExists(VerifyInputParameterCodeRequest verifyInputParameterCodeRequest);

    /**
     * 规则参数列表
     *
     * @param pageRequest param
     * @return p
     */
    PageResult<ListInputParameterResponse> list(PageRequest<ListInputParameterRequest> pageRequest);

    /**
     * 根据id查询规则参数
     *
     * @param id 规则参数id
     * @return r
     */
    GetInputParameterResponse get(Integer id);

    /**
     * 根据规则参数id更新规则参数
     *
     * @param updateInputParameterRequest 规则参数信息
     * @return true
     */
    Boolean update(UpdateInputParameterRequest updateInputParameterRequest);

    /**
     * 根据id删除规则参数
     *
     * @param id 规则参数id
     * @return true
     */
    Boolean delete(Integer id);
}
