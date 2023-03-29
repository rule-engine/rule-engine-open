/*
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.core.value;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.exception.FormulaException;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.ast.SpelNodeImpl;
import org.springframework.expression.spel.ast.VariableReference;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 表达式
 * <p>
 * 注意：目前表达式只能引用规则入参
 * <p>
 * 使用方式：
 * <blockquote>
 * <pre>
 *      expression = "(#param1 - #param2) * 3"
 *      input = {"param1":3,"param2":1}
 *      return 6;
 * </pre>
 * </blockquote>
 *
 * @author 丁乾文
 * @date 2021/7/17
 * @since 1.0.0
 */
@Slf4j
@ToString
public class Formula implements Value {

    /**
     * spel表达式解析器
     */
    private final static ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();


    /**
     * 表达式
     * <p>
     * setValue时会初始化expression 以及inputParameterCodes
     */
    @Getter
    private String value;

    private Expression expression;
    /**
     * 表达式返回类型
     */
    private ValueType valueType;

    /**
     * 表达式用到的所有的参数，主要用来获取并校验请求参数
     */
    private Set<String> inputParameterCodes = new HashSet<>();

    /**
     * EvaluationContextType 默认使用 SIMPLE_EVALUATION_READ_ONLY
     */
    private EvaluationContextType evaluationContextType = EvaluationContextType.SIMPLE_EVALUATION_READ_ONLY;

    /**
     * json反序列化使用
     */
    Formula() {

    }


    public Formula(@NonNull String value, @NonNull ValueType valueType) {
        Objects.requireNonNull(valueType);
        this.setValue(value);
        this.valueType = valueType;
    }

    public enum EvaluationContextType {
        /**
         * 谨慎使用 STANDARD_EVALUATION
         */
        SIMPLE_EVALUATION_READ_ONLY,
        SIMPLE_EVALUATION_READ_WRITE,
        STANDARD_EVALUATION
    }

    /**
     * 安全漏洞
     *
     * @param evaluationContextType 导入json文件可能被篡改
     */
    @Deprecated
    public void setEvaluationContextType(EvaluationContextType evaluationContextType) {
        Objects.requireNonNull(evaluationContextType);
        if (log.isWarnEnabled()) {
            if (evaluationContextType == EvaluationContextType.STANDARD_EVALUATION) {
                log.warn("请谨慎使用：EvaluationContextType = STANDARD_EVALUATION，有可能引起被攻击风险！");
            }
        }
        this.evaluationContextType = evaluationContextType;
    }

    /**
     * 解析表达式值
     *
     * @param input         上下文
     * @param configuration 规则配置信息
     * @return value
     */
    @Override
    public Object getValue(Input input, RuleEngineConfiguration configuration) {
        if (log.isDebugEnabled()) {
            log.debug("开始处理表达式：" + this.getValue());
        }
        EvaluationContext context = this.getEvaluationContext();
        if (!this.inputParameterCodes.isEmpty()) {
            for (String inputParameterCode : this.inputParameterCodes) {
                Object paramValue = input.get(inputParameterCode);
                if (Validator.isEmpty(paramValue)) {
                    throw new FormulaException("{} can not be null：", inputParameterCode);
                }
                context.setVariable(inputParameterCode, paramValue);
            }
        }
        Class<?> classType = this.valueType.getClassType();
        Object value;
        try {
            value = this.expression.getValue(context, classType);
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("表达式执行失败", e);
            }
            throw new FormulaException("表达式执行失败：" + e.getMessage());
        }
        return this.dataConversion(value, this.valueType);
    }

    /**
     * 当setValue时，并重置表达式解析
     *
     * @param value 表达式
     */
    public void setValue(String value) {
        Objects.requireNonNull(value);
        this.value = value;
        // 重新初始化
        ExpressionProcessor formulaProcessor = new ExpressionProcessor(value);
        this.expression = formulaProcessor.getExpression();
        // 处理表达式所有的参数，并缓存code
        this.inputParameterCodes = formulaProcessor.getInputParameterCodes();
    }


    public Set<String> getInputParameterCodes() {
        return Collections.unmodifiableSet(this.inputParameterCodes);
    }

    /**
     * get EvaluationContext
     *
     * @return EvaluationContext
     */
    private EvaluationContext getEvaluationContext() {
        switch (this.evaluationContextType) {
            case SIMPLE_EVALUATION_READ_ONLY:
                return SimpleEvaluationContext.forReadOnlyDataBinding().build();
            case SIMPLE_EVALUATION_READ_WRITE:
                return SimpleEvaluationContext.forReadWriteDataBinding().build();
            case STANDARD_EVALUATION:
                return new StandardEvaluationContext();
            default:
                throw new IllegalStateException("Unexpected value: " + evaluationContextType);
        }
    }

    @Override
    public ValueType getValueType() {
        return this.valueType;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Formula)) {
            return false;
        }
        Formula formula = (Formula) other;
        if (this.getValueType() != formula.getValueType()) {
            return false;
        }
        return Objects.equals(this.getValue(), formula.getValue());
    }


    public static class ExpressionProcessor {

        @Getter
        public String value;
        @Getter
        private final Expression expression;
        @Getter
        private final Set<String> inputParameterCodes;

        public ExpressionProcessor(String value) {
            this.value = value;
            this.expression = this.verifyAndGetFormula(value);
            this.inputParameterCodes = this.getExpressionParamCode(this.expression);
        }

        /**
         * 解析表达式中的所有参数code
         *
         * @param expression e
         * @return l
         */
        private Set<String> getExpressionParamCode(Expression expression) {
            Set<String> inputParameterCodes = new HashSet<>();
            if (((SpelExpression) expression).getAST() instanceof SpelNodeImpl) {
                SpelNodeImpl spelNode = (SpelNodeImpl) ((SpelExpression) expression).getAST();
                this.listSpelNodeVariable(inputParameterCodes, spelNode);
            }
            return inputParameterCodes;
        }

        /**
         * 获取节点中的所有的变量
         * <p>
         * #param
         *
         * @param spelNode spel节点
         */
        private void listSpelNodeVariable(Set<String> inputParameterCodes, SpelNode spelNode) {
            int childCount = spelNode.getChildCount();
            if (childCount == 0) {
                return;
            }
            for (int i = 0; i < childCount; i++) {
                SpelNode child = spelNode.getChild(i);
                if (child instanceof VariableReference) {
                    VariableReference variableReference = (VariableReference) child;
                    inputParameterCodes.add(variableReference.toStringAST().substring(1));
                }
                this.listSpelNodeVariable(inputParameterCodes, child);
            }
        }

        /**
         * 验证是否为合法的表达式
         *
         * @param value 表达式
         */
        public Expression verifyAndGetFormula(@NonNull String value) {
            try {
                return EXPRESSION_PARSER.parseExpression(value);
            } catch (IllegalStateException e) {
                throw new FormulaException("表达式编写错误:" + e.getMessage());
            }
        }

    }

}
