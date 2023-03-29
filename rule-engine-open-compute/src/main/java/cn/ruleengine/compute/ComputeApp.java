package cn.ruleengine.compute;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 〈App〉
 *
 * @author 丁乾文
 * @date 2021/6/17 8:35 下午
 * @since 1.0.0
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        RabbitAutoConfiguration.class
},
        scanBasePackages = "cn.ruleengine.compute")
@MapperScan({"cn.ruleengine.compute.store.mapper"})
@Import(RestTemplate.class)
public class ComputeApp {

    public static void main(String[] args) {
        SpringApplication.run(ComputeApp.class, args);
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
