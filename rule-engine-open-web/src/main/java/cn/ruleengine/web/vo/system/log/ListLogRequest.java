package cn.ruleengine.web.vo.system.log;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/3/2
 * @since 1.0.0
 */
@Data
public class ListLogRequest {


    private String tag;

    /**
     * 精确
     */
    private String requestId;

    /**
     * 模糊
     */
    private String requestUrl;

    /**
     * 模糊
     */
    private String username;

}
