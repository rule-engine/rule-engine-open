/*
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.web;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * <p>
 * 启动优化参数:
 * -server -Xms4096m -Xmx4096m  -Xss1024k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC
 * 说明:
 * -XX:+UseParallelOldGC:配置老年代垃圾收集器为并行收集。JDK6.0支持对老年代并行收集。
 * SurvivorRatio:年轻代中Eden区与两个Survivor区的比值
 * <p>
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@EnableAsync
@EnableScheduling
@EnableCaching(order = -2)
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        RabbitAutoConfiguration.class
},
        scanBasePackages = "cn.ruleengine.web")
@MapperScan({"cn.ruleengine.web.store.mapper"})
@EnableTransactionManagement
@Import({RestTemplate.class})
@EnableSwagger2
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableSwaggerBootstrapUI
public class WebApp {

    /**
     * boot-engine启动方法
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
        // logo
        System.out.println("__________               __     __________      .__          \n" +
                "\\______   \\ ____   _____/  |_   \\______   \\__ __|  |   ____  \n" +
                " |    |  _//  _ \\ /  _ \\   __\\   |       _/  |  \\  | _/ __ \\ \n" +
                " |    |   (  <_> |  <_> )  |     |    |   \\  |  /  |_\\  ___/ \n" +
                " |______  /\\____/ \\____/|__|     |____|_  /____/|____/\\___  >\n" +
                "        \\/                              \\/                \\/ ");
    }

    /**
     * 允许跨域请求
     *
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // 允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedOrigin(CorsConfiguration.ALL);
        // 允许访问的头信息,*表示全部
        config.addAllowedHeader(CorsConfiguration.ALL);
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        // 允许提交请求的方法，*表示全部允许
        config.addAllowedMethod(RequestMethod.GET.name());
        config.addAllowedMethod(RequestMethod.POST.name());
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}

