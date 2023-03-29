package cn.ruleengine.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@AllArgsConstructor
public enum HtmlTemplatesEnum {
    /**
     * 发送验证码的html模板
     */
    EMAIL("VerifyCode.ftl", "验证码"),
    /**
     * 发送异常警告的html模板
     */
    EXCEPTION("ExceptionMessage.ftl", "异常警告");

    @Getter
    String value;
    @Getter
    String msg;
}
