package cn.ruleengine.compute.function;

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 字母转小写
 *
 * @author 丁乾文
 * @date 2020/12/24
 * @since 1.0.0
 */
@Function
public class LetterToLowerCaseFunction {

    @Executor
    public String executor(@Param(value = "letter",required = false) String letter) {
        if (letter == null) {
            return null;
        }
        return letter.toLowerCase();
    }

}
