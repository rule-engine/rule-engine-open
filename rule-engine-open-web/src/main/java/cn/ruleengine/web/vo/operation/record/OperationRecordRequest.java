package cn.ruleengine.web.vo.operation.record;

import cn.ruleengine.web.enums.DataType;
import lombok.Data;

/**
 * 〈OperationRecordRequest〉
 *
 * @author 丁乾文
 * @date 2021/9/9 5:26 下午
 * @since 1.0.0
 */
@Data
public class OperationRecordRequest {


    private Integer userId;
    private String dataCode;
    private Integer dataId;
    private String dataName;
    private String version;
    private DataType dataType;

}
