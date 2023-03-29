package cn.ruleengine.compute.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.ruleengine.compute.service.WorkspaceService;
import cn.ruleengine.compute.store.entity.RuleEngineWorkspace;
import cn.ruleengine.compute.store.manager.RuleEngineWorkspaceManager;
import cn.ruleengine.compute.vo.AccessKey;
import cn.ruleengine.core.exception.ValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/21
 * @since 1.0.0
 */
@Slf4j
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Resource
    private RuleEngineWorkspaceManager ruleEngineWorkspaceManager;

    /**
     * 内存缓存
     */
    private final LRUCache<String, AccessKey> accessKeyCache = CacheUtil.newLRUCache(100);


    /**
     * 根据工作空间code获取AccessKey
     *
     * @param code 工作空间code
     * @return AccessKey
     */
    @Override
    public AccessKey accessKey(String code) {
        AccessKey accessKey = this.accessKeyCache.get(code);
        if (accessKey != null) {
            return accessKey;
        }
        RuleEngineWorkspace engineWorkspace = this.ruleEngineWorkspaceManager.lambdaQuery()
                .eq(RuleEngineWorkspace::getCode, code).one();
        if (engineWorkspace == null) {
            throw new ValidException("找不到此工作空间：" + code);
        }
        accessKey = new AccessKey();
        accessKey.setId(engineWorkspace.getId());
        accessKey.setAccessKeyId(engineWorkspace.getAccessKeyId());
        accessKey.setAccessKeySecret(engineWorkspace.getAccessKeySecret());
        this.accessKeyCache.put(code, accessKey, 1000 * 60 * 60);
        return accessKey;
    }

}
