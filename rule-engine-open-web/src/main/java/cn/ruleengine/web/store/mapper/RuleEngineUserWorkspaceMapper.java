package cn.ruleengine.web.store.mapper;

import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.web.store.entity.RuleEngineUserWorkspace;
import cn.ruleengine.web.vo.workspace.member.WorkspaceMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户工作空间 Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2021-06-22
 */
public interface RuleEngineUserWorkspaceMapper extends BaseMapper<RuleEngineUserWorkspace> {

    /**
     * 统计所有人员
     *
     * @param workspaceId 空间id
     * @param userName    用户名称  模糊查询
     * @param type
     * @return t
     */
    Integer totalMember(@Param("workspaceId") Integer workspaceId, @Param("userName") String userName, @Param("type") Integer type);

    /**
     * 所有人员信息
     *
     * @param workspaceId 空间id
     * @param userName    用户名称  模糊查询
     * @param page        p
     * @return r
     */
    List<WorkspaceMember> listMember(@Param("workspaceId") Integer workspaceId, @Param("userName") String userName, @Param("type") Integer type, @Param("page") PageBase page);

}
