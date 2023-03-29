package cn.ruleengine.web.vo.variable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class ListVariableResponse {
    private Integer id;

    private String name;

    private String description;

    private String valueType;

    private String functionName;

    private Integer type;

    private String value;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
