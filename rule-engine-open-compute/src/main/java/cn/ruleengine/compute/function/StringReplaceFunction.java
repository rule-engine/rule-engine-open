package cn.ruleengine.compute.function;

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/18
 * @since 1.0.0
 */
@Slf4j
@Function
public class StringReplaceFunction {

    @Executor
    public String executor(@Valid Params params) {
        String value = params.getValue();
        if (value == null) {
            return null;
        }
        String replacement = params.getReplacement();
        return value.replace(params.getTarget(), replacement);
    }

    @Data
    public static class Params {

        private String value;

        @NotNull
        private String target;

        @NotNull
        private String replacement;

    }

}
