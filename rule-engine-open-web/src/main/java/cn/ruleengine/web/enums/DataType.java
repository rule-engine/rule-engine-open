package cn.ruleengine.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 〈DataType〉
 *
 * @author 丁乾文
 * @date 2021/6/24 10:58 上午
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DataType {
    /**
     * 数据类型
     */
    GENERAL_RULE(0), RULE_SET(1), DECISION_TABLE(2), FUNCTION(6), INPUT_PARAMETER(7), VARIABLE(8), FORMULA(9);

    private final Integer type;


    /**
     * 转化数据类型枚举
     *
     * @param type t
     * @return DataType
     */
    public static DataType getByType(Integer type) {
        switch (type) {
            case 0:
                return GENERAL_RULE;
            case 1:
                return RULE_SET;
            case 2:
                return DECISION_TABLE;
            case 6:
                return FUNCTION;
            case 7:
                return INPUT_PARAMETER;
            case 8:
                return VARIABLE;
            case 9:
                return FORMULA;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    /**
     * 匹配是否为合法的类型
     *
     * @param type: 类型
     * @return boolean
     * @author nxq
     */
    public static boolean match(Integer type) {
        return Stream.of(DataType.values()).map(DataType::getType).anyMatch(a -> Objects.equals(type, a));
    }

}
