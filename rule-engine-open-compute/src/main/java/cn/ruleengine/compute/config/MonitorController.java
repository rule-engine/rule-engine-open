package cn.ruleengine.compute.config;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈MonitorController〉
 *
 * @author 丁乾文
 * @date 2021/9/9 8:50 下午
 * @since 1.0.0
 */
@Api(tags = "监控控制器")
@RestController
@RequestMapping("monitor")
public class MonitorController {


    @PostMapping("health")
    public boolean monitorHealth() {
        return true;
    }

}
