package cn.ruleengine.web.vo.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class DownloadListResponse {

    private Integer id;

    private Integer dataId;

    private String code;

    private String name;

    private String version;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
