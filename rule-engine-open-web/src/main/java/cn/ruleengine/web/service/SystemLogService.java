package cn.ruleengine.web.service;

import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.vo.system.log.GetLogResponse;
import cn.ruleengine.web.vo.system.log.ListLogRequest;
import cn.ruleengine.web.vo.system.log.ListLogResponse;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/3/2
 * @since 1.0.0
 */
public interface SystemLogService {

    /**
     * 查询日志列表
     *
     * @param pageRequest 分页参数
     * @return list
     */
    PageResult<ListLogResponse> list(PageRequest<ListLogRequest> pageRequest);

    /**
     * 根据id删除日志，只能由管理删除
     *
     * @param id 日志id
     * @return true
     */
    Boolean delete(Integer id);

    /**
     * 根据id查询日志详情
     *
     * @param id 日志id
     * @return info
     */
    GetLogResponse get(Integer id);

}
