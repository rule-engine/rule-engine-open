package cn.ruleengine.web.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author dqw
 * @since 2020-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RuleEngineSystemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String username;

    private String description;

    private String tag;

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
    @TableField(value = "`system`")
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

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}
