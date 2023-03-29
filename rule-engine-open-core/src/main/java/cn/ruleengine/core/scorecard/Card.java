package cn.ruleengine.core.scorecard;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.condition.ConditionSet;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 〈Card〉
 *
 * @author 丁乾文
 * @date 2019/8/13
 * @since 1.0.0
 */
@Data
public class Card {

    private Integer id;

    private String code;

    private String name;

    private String description;

    /**
     * 0分
     */
    public final static BigDecimal ZERO = new BigDecimal(0L);

    /**
     * 当条件全部满足时候返回此规则结果
     */
    private ConditionSet conditionSet = new ConditionSet();

    /**
     * 分值
     */
    private BigDecimal score;

    @Nullable
    protected BigDecimal execute(@NonNull Input input, @NonNull RuleEngineConfiguration configuration) {
        // 比较规则条件集
        if (this.conditionSet.compare(input, configuration)) {
            // 条件全部命中时候执行
            return this.score;
        }
        return ZERO;
    }


    public void setConditionSet(ConditionSet conditionSet) {
        this.conditionSet = Objects.requireNonNull(conditionSet);
    }


}
