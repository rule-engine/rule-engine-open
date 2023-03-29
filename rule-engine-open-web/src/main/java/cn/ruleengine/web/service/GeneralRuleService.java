package cn.ruleengine.web.service;

import cn.ruleengine.common.vo.IdRequest;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.vo.common.DownloadListResponse;
import cn.ruleengine.web.vo.common.GoBackRequest;
import cn.ruleengine.web.vo.common.HistoryListResponse;
import cn.ruleengine.web.vo.common.ViewRequest;
import cn.ruleengine.web.vo.generalrule.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
public interface GeneralRuleService {

    /**
     * 规则列表
     *
     * @param pageRequest 分页查询数据
     * @return page
     */
    PageResult<ListGeneralRuleResponse> list(PageRequest<ListGeneralRuleRequest> pageRequest);

    /**
     * 获取规则信息
     *
     * @param id 规则id
     * @return 规则信息
     */
    GetGeneralRuleResponse getRuleConfig(Integer id);

    /**
     * 规则code是否存在
     *
     * @param code 规则code
     * @return true存在
     */
    Boolean ruleCodeIsExists(String code);


    /**
     * 删除规则
     *
     * @param id 规则id
     * @return true
     */
    Boolean delete(Integer id);


    /**
     * 获取规则定义信息
     *
     * @param id 规则id
     * @return 规则定义信息
     */
    GeneralRuleDefinition getRuleDefinition(Integer id);

    /**
     * 生成测试版本，更新规则数据
     *
     * @param generalRuleBody 规则配置数据
     * @return true
     */
    Boolean generationRelease(GeneralRuleBody generalRuleBody);

    /**
     * 规则发布
     *
     * @param id 规则id
     * @return true
     */
    Boolean publish(Integer id);

    /**
     * 规则预览
     *
     * @param viewRequest 规则id
     * @return GetRuleResponse
     */
    ViewGeneralRuleResponse view(ViewRequest viewRequest);

    /**
     * 更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    Boolean updateRuleDefinition(GeneralRuleDefinition ruleDefinition);

    /**
     * 保存规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    Integer saveRuleDefinition(GeneralRuleDefinition ruleDefinition);

    /**
     * 保存默认结果
     *
     * @param saveActionRequest 保存默认结果
     * @return true
     */
    Boolean saveDefaultAction(SaveDefaultActionRequest saveActionRequest);

    /**
     * 是否开启默认结果
     *
     * @param defaultActionSwitchRequest 是否开启默认结果
     * @return true
     */
    Boolean defaultActionSwitch(DefaultActionSwitchRequest defaultActionSwitchRequest);

    /**
     * 规则下载列表
     *
     * @param pageRequest 规则id
     * @return true
     */
    PageResult<DownloadListResponse> downloadList(PageRequest<IdRequest> pageRequest);

    /**
     * 查询历史版本
     *
     * @param pageRequest 规则id
     * @return true
     */
    PageResult<HistoryListResponse> historyList(PageRequest<IdRequest> pageRequest);

    /**
     * 规则回退
     *
     * @param goBackRequest 回退版本信息
     * @return true
     */
    Boolean goBack(GoBackRequest goBackRequest);

    /**
     * 删除历史规则
     *
     * @param id 删除历史规则
     * @return true
     */
    Boolean deleteHistoricalRules(Integer id);

    /**
     * 已发布规则列表
     *
     * @param publishListRequest p
     * @return r
     */
    PageResult<PublishListResponse> referenceableList(PageRequest<PublishListRequest> publishListRequest);

}
