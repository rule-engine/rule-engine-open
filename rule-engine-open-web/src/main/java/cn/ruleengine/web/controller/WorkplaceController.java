package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.service.WorkplaceService;
import cn.ruleengine.web.vo.workplace.HeadInfoResponse;
import cn.ruleengine.web.vo.workplace.NumberOfCreationsRankingResponse;
import cn.ruleengine.web.vo.workplace.ProjectInProgressResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈WorkplaceController〉
 *
 * @author 丁乾文
 * @date 2021/9/9 1:13 下午
 * @since 1.0.0
 */
@Api(tags = "工作台控制器")
@RestController
@RequestMapping("ruleEngine/workplace")
public class WorkplaceController {

    @Resource
    private WorkplaceService workplaceService;


    /**
     * 进行中的项目
     *
     * @return list
     */
    @PostMapping("/projectInProgress")
    @ApiOperation("进行中的项目")
    public PlainResult<List<ProjectInProgressResponse>> projectInProgress() {
        PlainResult<List<ProjectInProgressResponse>> plainResult = new PlainResult<>();
        plainResult.setData(this.workplaceService.projectInProgress());
        return plainResult;
    }

    /**
     * HeadInfo
     *
     * @return r
     */
    @PostMapping("/headInfo")
    @ApiOperation("HeadInfo")
    public PlainResult<HeadInfoResponse> headInfo() {
        PlainResult<HeadInfoResponse> plainResult = new PlainResult<>();
        plainResult.setData(this.workplaceService.headInfo());
        return plainResult;
    }


    /**
     * 创作数量排名
     *
     * @return r
     */
    @PostMapping("/numberOfCreationsRanking")
    @ApiOperation("创作数量排名")
    public PlainResult<List<NumberOfCreationsRankingResponse>> numberOfCreationsRanking() {
        PlainResult<List<NumberOfCreationsRankingResponse>> plainResult = new PlainResult<>();
        plainResult.setData(this.workplaceService.numberOfCreationsRanking());
        return plainResult;
    }


}
