package cn.ruleengine.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/11
 * @since 1.0.0
 */
@AllArgsConstructor
public enum FunctionSource {
    /**
     *
     */
    SYSTEM(0), JAVA_CODE(1);

    @Getter
    private final Integer value;

}
