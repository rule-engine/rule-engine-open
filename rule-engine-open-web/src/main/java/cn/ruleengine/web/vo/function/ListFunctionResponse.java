package cn.ruleengine.web.vo.function;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Data
public class ListFunctionResponse {

    private Integer id;

    private String name;

    private String executor;

    private String returnValueType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 函数中所有的参数
     */
    private List<FunctionParam> params;

}
