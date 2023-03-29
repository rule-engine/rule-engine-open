package cn.ruleengine.web.enums;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
public enum RateLimitEnum {

    /**
     * 根据请求ip限流
     */
    IP,
    /**
     * 根据请求url限流
     */
    URL,
    /**
     * 根据用户限流,前提需要用户已经登录的情况下
     */
    USER,
    /**
     * 解决的问题是，如果用户访问url1导致根据ip地址限流了，但是访问url2也会无法访问
     */
    URL_IP
}
