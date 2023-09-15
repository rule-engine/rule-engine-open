package cn.ruleengine.web.service;

import cn.ruleengine.core.Parameter;
import cn.ruleengine.web.vo.reference.ReferenceData;
import cn.ruleengine.web.vo.reference.ReferenceDataMap;
import cn.ruleengine.web.vo.rule.general.GeneralRuleBody;

import java.util.Collection;
import java.util.Map;

/**
 * 〈DataReferenceService〉
 *
 * @author 丁乾文
 * @date 2021/7/27 2:21 下午
 * @since 1.0.0
 */
public interface DataReferenceService {

    /**
     * 是否有引用这个数据
     *
     * @param type      元素、变量、条件、规则等
     * @param refDataId 元素id...
     */
    void validDataReference(Integer type, Integer refDataId);

    /**
     * 保存规则引用的基础数据
     *
     * @param generalRuleBody g
     * @param version         版本
     */
    void saveDataReference(GeneralRuleBody generalRuleBody, String version);


    /**
     * 引用的参数
     *
     * @param referenceData r
     * @return map
     */
    Map<String, Parameter> referenceInputParamList(ReferenceData referenceData);


    /**
     * 更新到开发状态
     *
     * @param dataType d
     * @param dataId   id
     */
    void updateToDevStatus(Integer dataType, Integer dataId);

    /**
     * 缓存数据到ReferenceDataMap
     *
     * @param dataType 数据类型
     * @param dataId   id
     * @param version  版本号
     * @return r
     */
    ReferenceDataMap getReferenceDataMap(Integer dataType, Integer dataId, String version);

    /**
     * 获取规则集请求参数
     *
     * @param id      规则集id
     * @param version 版本
     * @return param
     */
    Collection<Parameter> getRuleSetParameters(Integer id, String version);

    /**
     * 获取规则请求参数
     *
     * @param id      规则集id
     * @param version 版本
     * @return param
     */
    Collection<Parameter> getGeneralRuleParameters(Integer id, String version);
}
