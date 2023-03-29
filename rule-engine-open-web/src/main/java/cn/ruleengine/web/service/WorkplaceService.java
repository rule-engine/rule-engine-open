package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.workplace.HeadInfoResponse;
import cn.ruleengine.web.vo.workplace.NumberOfCreationsRankingResponse;
import cn.ruleengine.web.vo.workplace.ProjectInProgressResponse;

import java.util.List;

/**
 * 〈WorkplaceService〉
 *
 * @author 丁乾文
 * @date 2021/9/9 1:14 下午
 * @since 1.0.0
 */
public interface WorkplaceService {

    /**
     * 进行中的项目
     *
     * @return  list
     */
    List<ProjectInProgressResponse> projectInProgress();

    /**
     * HeadInfo
     *
     * @return r
     */
    HeadInfoResponse headInfo();

    /**
     * 创作数量排名
     *
     * @return r
     */
    List<NumberOfCreationsRankingResponse> numberOfCreationsRanking();

}
