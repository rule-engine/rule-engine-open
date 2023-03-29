package cn.ruleengine.web.listener.event;

import cn.ruleengine.web.store.entity.RuleEngineSystemLog;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020-12-23 17:55
 * @since 1.0.0
 */
public class SystemLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1628296277627810450L;

    @Getter
    private final RuleEngineSystemLog ruleEngineSystemLog;

    public SystemLogEvent(RuleEngineSystemLog log) {
        super(log);
        this.ruleEngineSystemLog = log;
    }

}
