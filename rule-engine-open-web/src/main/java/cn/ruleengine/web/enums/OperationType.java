package cn.ruleengine.web.enums;

/**
 * 〈OperationType〉
 *
 * @author 丁乾文
 * @date 2021/6/24 11:00 上午
 * @since 1.0.0
 */
public enum OperationType {
    /**
     * 权限操作类型
     */
    ADD, DELETE, UPDATE, SELECT,
    /**
     * 校验发布权限
     */
    PUBLISH,
    /**
     * 修改数据权权限
     */
    DATE_DATA_PERMISSION
}
