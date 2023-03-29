package cn.ruleengine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.service.SystemLogService;
import cn.ruleengine.web.store.entity.RuleEngineSystemLog;
import cn.ruleengine.web.store.manager.RuleEngineSystemLogManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.system.log.GetLogResponse;
import cn.ruleengine.web.vo.system.log.ListLogRequest;
import cn.ruleengine.web.vo.system.log.ListLogResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/3/2
 * @since 1.0.0
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Resource
    private RuleEngineSystemLogManager ruleEngineSystemLogManager;

    /**
     * 查询日志列表
     *
     * @param pageRequest 分页参数
     * @return list
     */
    @Override
    public PageResult<ListLogResponse> list(PageRequest<ListLogRequest> pageRequest) {
        PageBase page = pageRequest.getPage();
        ListLogRequest query = pageRequest.getQuery();
        return PageUtils.page(this.ruleEngineSystemLogManager, page, () -> {
            QueryWrapper<RuleEngineSystemLog> logQueryWrapper = new QueryWrapper<>();
            PageUtils.defaultOrder(pageRequest.getOrders(), logQueryWrapper);
            LambdaQueryWrapper<RuleEngineSystemLog> lambda = logQueryWrapper.lambda();
            if (Validator.isNotEmpty(query.getUsername())) {
                lambda.like(RuleEngineSystemLog::getUsername, query.getUsername());
            }
            if (Validator.isNotEmpty(query.getRequestId())) {
                lambda.eq(RuleEngineSystemLog::getRequestId, query.getRequestId());
            }
            if (Validator.isNotEmpty(query.getRequestUrl())) {
                lambda.like(RuleEngineSystemLog::getRequestUrl, query.getRequestUrl());
            }
            if (Validator.isNotEmpty(query.getTag())) {
                lambda.eq(RuleEngineSystemLog::getTag, query.getTag());
            }
            return logQueryWrapper;
        }, m -> {
            ListLogResponse listLogResponse = new ListLogResponse();
            BeanUtil.copyProperties(m, listLogResponse);
            return listLogResponse;
        });
    }

    /**
     * 根据id删除日志，只能由管理删除
     *
     * @param id 日志id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        return this.ruleEngineSystemLogManager.removeById(id);
    }

    /**
     * 根据id查询日志详情
     *
     * @param id 日志id
     * @return info
     */
    @Override
    public GetLogResponse get(Integer id) {
        RuleEngineSystemLog ruleEngineSystemLog = this.ruleEngineSystemLogManager.getById(id);
        if (ruleEngineSystemLog == null) {
            return null;
        }
        GetLogResponse getLogResponse = new GetLogResponse();
        BeanUtil.copyProperties(ruleEngineSystemLog, getLogResponse);
        return getLogResponse;
    }

}
