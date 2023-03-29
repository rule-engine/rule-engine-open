package cn.ruleengine.web.service.impl;


import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.service.WorkplaceService;
import cn.ruleengine.web.store.entity.RuleEngineGeneralRule;
import cn.ruleengine.web.store.entity.RuleEngineUserWorkspace;
import cn.ruleengine.web.store.manager.RuleEngineGeneralRuleManager;
import cn.ruleengine.web.store.manager.RuleEngineUserWorkspaceManager;
import cn.ruleengine.web.store.mapper.WorkplaceMapper;
import cn.ruleengine.web.vo.workplace.HeadInfoResponse;
import cn.ruleengine.web.vo.workplace.NumberOfCreationsRankingResponse;
import cn.ruleengine.web.vo.workplace.ProjectInProgressResponse;
import cn.ruleengine.web.vo.workspace.Workspace;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈WorkplaceServiceImpl〉
 *
 * @author 丁乾文
 * @date 2021/9/9 1:14 下午
 * @since 1.0.0
 */
@Service
public class WorkplaceServiceImpl implements WorkplaceService {


    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;
    @Resource
    private WorkplaceMapper workplaceMapper;
    @Resource
    private RuleEngineUserWorkspaceManager ruleEngineUserWorkspaceManager;

    /**
     * 进行中的项目
     * 更新时间倒序
     *
     * @return list
     */
    @Override
    public List<ProjectInProgressResponse> projectInProgress() {
        Workspace currentWorkspace = Context.getCurrentWorkspace();
        return this.workplaceMapper.projectInProgress(currentWorkspace.getId());
    }

    /**
     * HeadInfo
     *
     * @return r
     */
    @Override
    public HeadInfoResponse headInfo() {
        Workspace currentWorkspace = Context.getCurrentWorkspace();
        HeadInfoResponse headInfoResponse = new HeadInfoResponse();
        headInfoResponse.setWorkspaceMemberNumber(this.ruleEngineUserWorkspaceManager.lambdaQuery()
                .eq(RuleEngineUserWorkspace::getWorkspaceId, currentWorkspace.getId())
                .count());
        headInfoResponse.setPublishedGeneralRuleNumber(this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getWorkspaceId, currentWorkspace.getId())
                .isNotNull(RuleEngineGeneralRule::getPublishVersion)
                .count());
        return headInfoResponse;
    }

    /**
     * 创作数量排名
     *
     * @return r
     */
    @Override
    public List<NumberOfCreationsRankingResponse> numberOfCreationsRanking() {
        Workspace currentWorkspace = Context.getCurrentWorkspace();
        return this.workplaceMapper.numberOfCreationsRanking(currentWorkspace.getId());
    }

}
