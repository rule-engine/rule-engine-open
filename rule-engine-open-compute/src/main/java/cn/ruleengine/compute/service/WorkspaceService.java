package cn.ruleengine.compute.service;

import cn.ruleengine.compute.vo.AccessKey;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/21
 * @since 1.0.0
 */
public interface WorkspaceService {

    /**
     * 当前工作空间AccessKey
     *
     * @param code 工作空间code
     * @return accessKey
     */
    AccessKey accessKey(String code);


}
