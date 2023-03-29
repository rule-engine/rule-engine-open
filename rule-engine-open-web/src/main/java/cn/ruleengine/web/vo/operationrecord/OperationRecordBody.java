package cn.ruleengine.web.vo.operationrecord;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 〈OperationRecordBody〉
 *
 * @author 丁乾文
 * @date 2021/7/13 6:25 下午
 * @since 1.0.0
 */
@Data
public class OperationRecordBody implements Serializable {

    private static final long serialVersionUID = 376107396179241194L;

    /**
     * 修改人
     */
    private Integer userId;

    /**
     * 修改人头像
     */
    private String avatar;


    /**
     * 修改描述
     */
    private String description;

    /**
     * 操作时间
     */
    private Date operationTime;

}
