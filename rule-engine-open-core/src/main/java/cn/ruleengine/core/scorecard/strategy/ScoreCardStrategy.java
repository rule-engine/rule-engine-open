package cn.ruleengine.core.scorecard.strategy;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.scorecard.Card;

import java.math.BigDecimal;
import java.util.List;

/**
 * 〈ScoreCardStrategy〉
 *
 * @author 丁乾文
 * @date 2019/8/13
 * @since 1.0.0
 */
public interface ScoreCardStrategy {

    BigDecimal compute(List<Card> cards, Input input, RuleEngineConfiguration configuration);
}
