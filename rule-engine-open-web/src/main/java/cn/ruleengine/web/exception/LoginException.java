package cn.ruleengine.web.exception;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/1/10
 * @since 1.0.0
 */
public class LoginException extends RuntimeException {

    private static final long serialVersionUID = 6405345374923437770L;

    public LoginException(String message) {
        super(message);
    }

}
