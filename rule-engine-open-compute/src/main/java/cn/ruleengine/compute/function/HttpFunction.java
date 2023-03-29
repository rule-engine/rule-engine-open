package cn.ruleengine.compute.function;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 〈HttpFunction〉
 *
 * @author 丁乾文
 * @date 2021/7/9 6:38 下午
 * @since 1.0.0
 */
@Slf4j
@Function
public class HttpFunction {

    @Executor
    public Object executor(@Valid Params params) {
        String method = params.getRequestMethod();
        String requestUrl = params.getRequestUrl();
        // TODO: 2021/7/9  待完成，需要处理替换表达式  先下班了
        String requestBody = params.getRequestBody();
        HttpResponse execute;
        if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
            //  ...
            JSONObject jsonObject = JSON.parseObject(requestBody);
            execute = HttpUtil.createGet(requestUrl).form(jsonObject).execute();
        } else {
            execute = HttpUtil.createPost(requestUrl).body(requestBody).execute();
        }
        String body = execute.body();
        log.info("请求响应：" + body);
        return body;
    }

    @Data
    public static class Params {

        @NotBlank
        private String requestUrl;

        @NotBlank
        private String requestMethod;

        /**
         * 请求body
         */
        private String requestBody;
    }

}
