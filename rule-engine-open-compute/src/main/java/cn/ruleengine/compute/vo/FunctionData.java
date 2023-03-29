package cn.ruleengine.compute.vo;

import lombok.Data;

import java.util.List;

/**
 * 〈FunctionData〉
 *
 * @author 丁乾文
 * @date 2021/7/9 4:25 下午
 * @since 1.0.0
 */
@Data
public class FunctionData {

    private Integer functionId;

    private String executor;

    private String returnValueType;

    private List<Value> values;

    @Data
    public static class Value {

        private String paramCode;

        private Integer type;

        private String valueType;

        private String value;

    }

}
