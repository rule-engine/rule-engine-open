package cn.ruleengine.compute.function;

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 返回一个随机UUID字符串。
 *
 * @author dingqianwen
 * @date 2021/2/9
 * @since 1.0.0
 */
@Function
@Slf4j
public class GetUUIDFunction {

    @Executor
    public String executor() {
        return UUID.randomUUID().toString();
    }

}
