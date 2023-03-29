package cn.ruleengine.compute.function.json;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/13
 * @since 1.0.0
 */
@Slf4j
@Function
public class ParseJsonCollectionFunction implements JsonEval {

    @Executor
    public List<?> executor(@Param(value = "jsonString", required = false) String jsonString,
                            @Param(value = "jsonValuePath", required = false) String jsonValuePath) {
        Object value = this.eval(jsonString, jsonValuePath);
        // 返回null 而不是String.valueOf后的null字符
        if (Validator.isEmpty(value)) {
            return Collections.emptyList();
        }
        if (value instanceof Collection) {
            // 支持{name:[1,2,3]}
            return (List<?>) value;
        } else {
            // 支持{name:'1,2,3'}
            return Arrays.asList(String.valueOf(value).split(","));
        }
    }

}
