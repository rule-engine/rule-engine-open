package cn.ruleengine.compute.function;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/19
 * @since 1.0.0
 */
@Function
public class CurrentDateFunction {

    @Executor
    public Date executor(@Param(value = "timeZone", required = false) String timeZone) {
        ZoneId zoneId = Optional.ofNullable(timeZone).filter(Validator::isNotEmpty).map(ZoneId::of).orElseGet(ZoneId::systemDefault);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

}
