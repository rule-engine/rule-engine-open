package cn.ruleengine.compute.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DataStatus {

    /**
     * 规则/决策表的各种状态
     */
    DEV(0), TEST(1), PRD(2),
    /**
     * 历史的线上
     */
    HISTORY(3);

    private final Integer status;

}
