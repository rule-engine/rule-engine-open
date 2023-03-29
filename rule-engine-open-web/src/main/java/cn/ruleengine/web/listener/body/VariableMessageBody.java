package cn.ruleengine.web.listener.body;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/17
 * @since 1.0.0
 */
@Data
public class VariableMessageBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private VariableMessageBody.Type type;

    private Integer id;

    public enum Type {
        /**
         * 规则加载，以及移除
         */
        LOAD, REMOVE, UPDATE
    }
}
