package cn.ruleengine.web.vo.workplace;

import lombok.Data;

/**
 * 〈NumberOfCreationsRankingResponse〉
 *
 * @author 丁乾文
 * @date 2021/9/9 2:23 下午
 * @since 1.0.0
 */
@Data
public class NumberOfCreationsRankingResponse {

    private Integer ruleSetNumber;

    private Integer generalRuleNumber;

    private String createUsername;

    private Integer createUserId;

}
