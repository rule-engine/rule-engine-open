package cn.ruleengine.web.util;


import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import javax.validation.ValidationException;
import java.lang.reflect.Method;

/**
 * 〈SpelUtils〉
 *
 * @author 丁乾文
 * @date 2021/9/9 4:00 下午
 * @since 1.0.0
 */
public class SpelUtils {

    /**
     * spel表达式解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    /**
     * resolveProceedingJoinPointSpel
     *
     * @param method 方法
     * @param args   参数对象数组
     * @param spel   s
     * @return r
     */
    public static <T> T resolveProceedingJoinPointSpel(Method method, Object[] args, String spel, Class<T> tClass) {
        //获取方法参数名
        String[] params = DISCOVERER.getParameterNames(method);
        if (params == null || params.length == 0) {
            throw new ValidationException("没有获取到任何参数");
        }
        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        Expression expression = PARSER.parseExpression(spel);
        return expression.getValue(context, tClass);
    }

}
