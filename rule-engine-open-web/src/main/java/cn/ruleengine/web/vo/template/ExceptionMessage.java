package cn.ruleengine.web.vo.template;

import cn.ruleengine.web.interceptor.TraceInterceptor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2024/9/18
 * @since 1.0.0
 */
@Data
public class ExceptionMessage {

    private String type;

    private LocalDateTime time = LocalDateTime.now();

    private String message;

    private String requestId = TraceInterceptor.getRequestId();

}
