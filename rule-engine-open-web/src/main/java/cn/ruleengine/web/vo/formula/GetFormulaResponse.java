package cn.ruleengine.web.vo.formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class GetFormulaResponse {

    private Integer id;

    private String name;

    private String value;

    private String valueType;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
