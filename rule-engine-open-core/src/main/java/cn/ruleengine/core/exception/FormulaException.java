package cn.ruleengine.core.exception;

import cn.hutool.core.text.StrFormatter;

/**
 * 〈FormulaException〉
 *
 * @author 丁乾文
 * @date 2021/7/19 1:27 下午
 * @since 1.0.0
 */
public class FormulaException extends ValueException {

    private static final long serialVersionUID = -8751437968246403242L;

    public FormulaException(String message) {
        super(message);
    }

    public FormulaException(String message, Object... args) {
        super(StrFormatter.format(message, args));
    }

}
