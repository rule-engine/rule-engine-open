package cn.ruleengine.web.vo.system.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/3/2
 * @since 1.0.0
 */
@Data
public class GetLogResponse {

    private Integer id;

    private Integer userId;

    private String username;

    private String tag;

    private String description;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 请求者系统
     */
    private String system;


    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求id
     */
    private String requestId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
