package cn.ruleengine.core.scorecard;

import lombok.AllArgsConstructor;

/**
 * 〈ScoreCardStrategyType〉
 *
 * @author 丁乾文
 * @date 2019/8/13
 * @since 1.0.0
 */
@AllArgsConstructor
public enum ScoreCardStrategyType {

    /**
     * 求和
     * <p>
     * 默认
     */
    SUM,
    /**
     * 平均分值
     */
    AVG,
    /**
     * 所有卡片中最大的那个分值
     */
    MIN,
    /**
     * 所有卡片中最小的那个分值
     */
    MAX;

}
