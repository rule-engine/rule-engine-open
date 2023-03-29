package cn.ruleengine.web.listener.event;

import cn.ruleengine.web.listener.body.VariableMessageBody;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020-12-23 18:07
 * @since 1.0.0
 */
public class VariableEvent extends ApplicationEvent {

    private static final long serialVersionUID = 7277630131038817515L;

    @Getter
    private final VariableMessageBody variableMessageBody;

    public VariableEvent(VariableMessageBody variableMessageBody) {
        super(variableMessageBody);
        this.variableMessageBody = variableMessageBody;
    }

}
