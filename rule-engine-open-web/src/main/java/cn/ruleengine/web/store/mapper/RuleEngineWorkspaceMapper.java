package cn.ruleengine.web.store.mapper;

import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.web.store.entity.RuleEngineWorkspace;
import cn.ruleengine.web.vo.workspace.ListWorkspaceRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工作空间 Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-11-21
 */
public interface RuleEngineWorkspaceMapper extends BaseMapper<RuleEngineWorkspace> {

    /**
     * 根据用户id获取用户有权限的工作空间
     *
     * @param userId 用户id
     * @return list
     */
    List<RuleEngineWorkspace> listWorkspaceByUserId(@Param("userId") Integer userId);


    /**
     * 根据用户id获取用户有权限的工作空间 带条件
     *
     * @param userId 用户id
     * @param query  条件
     * @param page   分页
     * @return RuleEngineWorkspace
     */
    List<RuleEngineWorkspace> listWorkspace(@Param("userId") Integer userId, @Param("query") ListWorkspaceRequest query, @Param("page") PageBase page);

    /**
     * 分页统计数量
     * <p>
     * 根据用户id获取用户有权限的工作空间 带条件
     *
     * @param userId 用户id
     * @param query  条件
     * @param page   分页
     * @return RuleEngineWorkspace
     */
    Integer totalWorkspace(@Param("userId") Integer userId, @Param("query") ListWorkspaceRequest query, @Param("page") PageBase page);

    /**
     * 获取第一条数据
     *
     * @return RuleEngineWorkspace
     */
    RuleEngineWorkspace getFirstWorkspace();
}
