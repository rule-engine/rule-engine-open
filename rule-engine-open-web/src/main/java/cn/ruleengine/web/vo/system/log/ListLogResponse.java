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
public class ListLogResponse {

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
     * 请求者系统详情
     */
    private String detailed;

    /**
     * 请求参数
     */
    private String ages;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 返回的数据
     */
    private String returnValue;

    /**
     * 请求结束时间
     */
    private Date endTime;

    /**
     * 运行时间
     */
    private Long runningTime;
    /**
     * 是否为移动平台
     */
    private Boolean mobile;

    /**
     * 异常
     */
    private String exception;

    /**
     * 请求id
     */
    private String requestId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
