package cn.ruleengine.web.vo.inputparameter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class ListInputParameterRequest {
    private String name;
    private String code;

    private List<String> valueType;
}
