package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.vo.workplace.NumberOfCreationsRankingResponse;
import cn.ruleengine.web.vo.workplace.ProjectInProgressResponse;

import java.util.List;

/**
 * 〈WorkplaceMapper〉
 *
 * @author 丁乾文
 * @date 2021/9/9 1:33 下午
 * @since 1.0.0
 */
public interface WorkplaceMapper {

    /**
     * projectInProgress
     *
     * @param id workId
     * @return r
     */
    List<ProjectInProgressResponse> projectInProgress(Integer id);

    /**
     * 创作数量排名
     *
     * @return r
     */
    List<NumberOfCreationsRankingResponse> numberOfCreationsRanking(Integer id);
}
