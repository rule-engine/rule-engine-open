package cn.ruleengine.compute.function.json;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import javax.validation.ValidationException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/13
 * @since 1.0.0
 */
public interface JsonEval {

    /**
     * json path解析
     *
     * @param jsonString    json字符串
     * @param jsonValuePath path
     * @return Object
     */
    default Object eval(String jsonString, String jsonValuePath) {
        if (Validator.isEmpty(jsonString)) {
            return StringPool.EMPTY;
        }
        if (Validator.isEmpty(jsonValuePath)) {
            throw new ValidationException("JSON值路径不能为空");
        }
        // fastjson JSONValidator 各种bug写的垃圾代码 所以使用hutool JSONUtil
        if (!JSONUtil.isJson(jsonString)) {
            throw new ValidationException("JSON格式错误:" + jsonString);
        }
        return JSONPath.eval(jsonString, jsonValuePath);
    }

}
