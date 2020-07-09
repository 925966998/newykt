package com.ky.ykt.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

@Configuration
public class AppConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public HandlerInterceptor getMyInterceptor() {
        return new Interceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePaths = {"/ky-ykt/reset", "/ky-ykt/loginOut", "/js/pdfJs/web/viewer.html", "/web/*", "/upload/*", "/ky-ykt/weChat", "/wechat/*"};
        registry.addInterceptor(getMyInterceptor()).excludePathPatterns(Arrays.asList(excludePaths)).addPathPatterns("/ky-ykt/**");
        super.addInterceptors(registry);
    }
}
