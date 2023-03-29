package cn.ruleengine.web.vo.operationrecord;

import lombok.Data;

import java.util.Date;

/**
 * 〈OperationRecordResponse〉
 *
 * @author 丁乾文
 * @date 2021/9/9 5:26 下午
 * @since 1.0.0
 */
@Data
public class OperationRecordResponse {
    private Integer id;

    private Integer userId;

    private String username;

    private String userAvatar;

    private Integer workspaceId;

    private String workspaceCode;

    private String description;

    private Integer dataType;

    private Integer dataId;

    private Date operationTime;
}
