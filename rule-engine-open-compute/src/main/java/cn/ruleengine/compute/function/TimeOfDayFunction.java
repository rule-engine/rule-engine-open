package cn.ruleengine.compute.function;

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/24
 * @since 1.0.0
 */
@Function(name = "当前时间段")
public class TimeOfDayFunction {

    @Executor
    public String executor() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(simpleDateFormat.format(date));
        if (hour >= 0 && hour < 6) {
            return "凌晨";
        }
        if (hour >= 6 && hour < 11) {
            return "上午";
        }
        // 中午是11时至13时。太阳在子午线上方时为正午，一般是12时左右
        if (hour >= 11 && hour < 13) {
            return "中午";
        }
        if (hour >= 13 && hour <= 18) {
            return "下午";
        }
        return "晚上";
    }


}
