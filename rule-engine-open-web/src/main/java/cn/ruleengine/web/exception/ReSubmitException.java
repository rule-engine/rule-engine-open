package cn.ruleengine.web.exception;

import cn.ruleengine.web.enums.ErrorCodeEnum;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/1/10
 * @since 1.0.0
 */
public class ReSubmitException extends RuntimeException {

    private static final long serialVersionUID = 6405345374923437770L;

    @Getter
    private final int code;

    public ReSubmitException() {
        super(ErrorCodeEnum.RULE10011038.getMsg());
        this.code = ErrorCodeEnum.RULE10011038.getCode();
    }

}
