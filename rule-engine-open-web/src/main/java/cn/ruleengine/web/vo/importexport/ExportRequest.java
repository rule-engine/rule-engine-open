package cn.ruleengine.web.vo.importexport;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 〈ExportRequest〉
 *
 * @author 丁乾文
 * @date 2021/7/13 5:55 下午
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExportRequest extends DataTypeAndId {

    private String version;

}
