package cn.ruleengine.core.condition;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/13
 * @since 1.0.0
 */
@Slf4j
public class Precondition implements ConditionCompare {


    private final List<Condition> precondition = new ArrayList<>();

    /**
     * 比较前提条件
     *
     * @param input         入参
     * @param configuration 规则引擎配置信息
     * @return 比较结果
     */
    @Override
    public boolean compare(Input input, RuleEngineConfiguration configuration) {
        if (CollUtil.isEmpty(this.precondition)) {
            return true;
        }
        for (Condition condition : this.precondition) {
            if (!condition.compare(input, configuration)) {
                log.debug("前提条件不成立:" + condition.getName());
                return false;
            }
        }
        return true;
    }

    /**
     * 添加前提条件
     *
     * @param condition 条件信息
     */
    public void addPrecondition(@NonNull Condition condition) {
        Objects.requireNonNull(condition);
        Condition.verify(condition);
        this.precondition.add(condition);
    }

    public List<Condition> getPrecondition() {
        return Collections.unmodifiableList(this.precondition);
    }

}
