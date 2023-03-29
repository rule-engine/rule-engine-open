package cn.ruleengine.compute.function;

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/24
 * @since 1.0.0
 */
@Function
public class DateToTimestampFunction {

    @Executor
    public Long executor(@Param(value = "date") Date date) {
        return date.getTime();
    }

}
