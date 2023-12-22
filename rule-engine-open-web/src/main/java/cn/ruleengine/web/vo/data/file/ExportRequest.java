package cn.ruleengine.web.vo.data.file;

import lombok.Data;

/**
 * 〈ExportRequest〉
 *
 * @author 丁乾文
 * @date 2021/7/13 5:55 下午
 * @since 1.0.0
 */
@Data
public class ExportRequest {

    private Integer dataType;

    /**
     * 数据库id
     * 如果data_type=0 则此data_id为规则的id
     */
    private Integer dataId;

    private String version;

}
