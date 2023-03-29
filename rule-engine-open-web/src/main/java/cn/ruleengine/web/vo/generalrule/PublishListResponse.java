package cn.ruleengine.web.vo.generalrule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 〈PublishListRequest〉
 *
 * @author 丁乾文
 * @date 2021/7/28 9:04 下午
 * @since 1.0.0
 */
@Data
public class PublishListResponse {

    /**
     * 普通规则id
     */
    private Integer id;

    private String name;

    private String code;

    private String valueType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
