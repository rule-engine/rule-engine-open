package cn.ruleengine.web.enums;

import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Getter
public enum EnableEnum {

    /**
     * ENABLE
     */
    ENABLE(0), DISABLE(1);

    private final Integer status;

    EnableEnum(Integer status) {
        this.status = status;
    }

}
