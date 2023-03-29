package cn.ruleengine.web.vo.workplace;

import lombok.Data;

import java.util.Date;

/**
 * 〈ProjectInProgressResponse〉
 *
 * @author 丁乾文
 * @date 2021/9/9 1:15 下午
 * @since 1.0.0
 */
@Data
public class ProjectInProgressResponse {

    private Integer id;

    private Integer dataType;

    private String createUsername;

    private String name;

    private String code;

    private String currentVersion;

    private Integer status;

    private Date updateTime;

    private String description;

}
