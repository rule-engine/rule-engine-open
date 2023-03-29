package cn.ruleengine.core.scorecard.strategy;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.scorecard.Card;

import java.math.BigDecimal;
import java.util.List;

/**
 * 〈AvgStrategy〉
 *
 * @author 丁乾文
 * @date 2019/8/13
 * @since 1.0.0
 */
public class AvgStrategy implements ScoreCardStrategy {

    private static final AvgStrategy INSTANCE = new AvgStrategy();

    public static AvgStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public BigDecimal compute(List<Card> cards, Input input, RuleEngineConfiguration configuration) {
        return null;
    }

}
