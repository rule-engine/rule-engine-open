package cn.ruleengine.core.value;

import cn.ruleengine.core.Container;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.exception.ValueException;
import cn.ruleengine.core.rule.GeneralRule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 〈Execute〉
 * <p>
 * 执行规则
 * <p>
 * 目前可以执行基本规则
 *
 * @author 丁乾文
 * @date 2021/7/22 10:10 上午
 * @since 1.0.0
 */
@Slf4j
public class Executor implements Value {

    @Getter
    private Integer id;
    @Getter
    private String code;
    @Getter
    private String workspaceCode;

    private ValueType valueType;

    /**
     * json 反序列化使用
     */
    Executor() {

    }

    public Executor(String workspaceCode, Integer id, String code, ValueType valueType) {
        this.workspaceCode = Objects.requireNonNull(workspaceCode);
        this.code = Objects.requireNonNull(code);
        this.id = Objects.requireNonNull(id);
        this.valueType = Objects.requireNonNull(valueType);
    }

    @Override
    public Object getValue(Input input, RuleEngineConfiguration configuration) {
        if (log.isDebugEnabled()) {
            log.debug("执行规则:{}-{}", this.workspaceCode, this.code);
        }
        Container.Body<GeneralRule> generalRuleContainer = configuration.getGeneralRuleContainer();
        GeneralRule generalRule = generalRuleContainer.get(this.workspaceCode, this.code);
        if (generalRule == null) {
            throw new EngineException("no general rule:{}", code);
        }
        Object action = generalRule.execute(input, configuration);
        // 如果执行结果为空，则不再校验类型，直接返回，进去后续逻辑判断
        if (action == null) {
            return null;
        }
        Class<?> classType = action.getClass();
        if (!classType.isAssignableFrom(valueType.getClassType())) {
            throw new ValueException("基础规则执行结果与配置结果类型不同：{}-{}", classType, valueType.getClassType());
        }
        return action;
    }

    @Override
    public ValueType getValueType() {
        return this.valueType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Executor executor = (Executor) o;
        return Objects.equals(id, executor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
