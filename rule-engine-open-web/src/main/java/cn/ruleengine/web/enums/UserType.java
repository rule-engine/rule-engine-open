package cn.ruleengine.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈UserType〉
 *
 * @author 丁乾文
 * @date 2021/6/22 6:28 下午
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum UserType {
    /**
     * 超级管理
     */
    SUPER_ADMINISTRATOR(0),
    /**
     * 工作空间管理
     */
    WORKSPACE_ADMINISTRATOR(1),
    /**
     * 普通用户
     */
    GENERAL_USER(2);

    private final Integer type;

}
