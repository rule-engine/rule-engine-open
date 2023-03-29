package cn.ruleengine.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/24
 * @since 1.0.0
 */
@AllArgsConstructor
public enum VerifyCodeType {
    /**
     * 忘记密码/注册  获取验证码
     */
    FORGOT(0), REGISTER(1);

    @Getter
    private final Integer value;

}
