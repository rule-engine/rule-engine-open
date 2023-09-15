package cn.ruleengine.web.vo.rule.general;

import lombok.Data;

import java.util.List;

/**
 * 〈PublishListRequest〉
 *
 * @author 丁乾文
 * @date 2021/7/28 9:04 下午
 * @since 1.0.0
 */
@Data
public class PublishListRequest{

    private String name;
    private String code;

    private List<String> valueType;

}
