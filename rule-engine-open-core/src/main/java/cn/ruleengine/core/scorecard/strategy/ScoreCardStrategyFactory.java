package cn.ruleengine.core.scorecard.strategy;

import cn.ruleengine.core.scorecard.ScoreCardStrategyType;

/**
 * 〈ScoreCardStrategyFactroy〉
 *
 * @author 丁乾文
 * @date 2019/8/13
 * @since 1.0.0
 */
public class ScoreCardStrategyFactory {

    public static ScoreCardStrategy getInstance(ScoreCardStrategyType scoreCardStrategyType) {
        switch (scoreCardStrategyType) {
            case SUM:
                return SumStrategy.getInstance();
            case AVG:
                return AvgStrategy.getInstance();
            case MIN:
                return MinStrategy.getInstance();
            case MAX:
                return MaxStrategy.getInstance();
            default:
                throw new IllegalStateException("Unexpected value: " + scoreCardStrategyType);
        }
    }

}
