package cn.ruleengine.core;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/28
 * @since 1.0.0
 */
@Data
public class Parameter {

    /**
     * 规则参数名称
     */
    private String name;

    /**
     * 规则参数code
     */
    private String code;

    /**
     * 规则参数类型
     */
    private String valueType;

}
