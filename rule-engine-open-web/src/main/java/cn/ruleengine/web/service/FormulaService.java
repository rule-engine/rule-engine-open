package cn.ruleengine.web.service;

import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.vo.formula.*;

/**
 * @author Administrator
 */
public interface FormulaService {

    /**
     * 表达式列表
     *
     * @param pageRequest param
     * @return r
     */
    PageResult<FormulaListResponse> list(PageRequest<FormulaListRequest> pageRequest);

    /**
     * 保存表达式
     *
     * @param saveFormulaRequest param
     * @return r
     */
    Boolean saveFormula(SaveFormulaRequest saveFormulaRequest);

    /**
     * 更新表达式
     *
     * @param updateFormulaRequest param
     * @return r
     */
    Boolean updateFormula(UpdateFormulaRequest updateFormulaRequest);

    /**
     * 根据id获取表达式
     *
     * @param id param
     * @return r
     */
    GetFormulaResponse getFormula(Integer id);

    /**
     * 删除表达式
     *
     * @param id param
     * @return r
     */
    Boolean deleteFormula(Integer id);

    /**
     * 验证表达式名称是否存在
     *
     * @param validationExpressionNameRequest v
     * @return r
     */
    Boolean validationExpressionName(ValidationExpressionNameRequest validationExpressionNameRequest);
}
