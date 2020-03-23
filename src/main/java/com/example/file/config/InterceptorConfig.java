package com.example.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 允许跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                .maxAge(3600);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //排除拦截
        List<String> exc = new ArrayList<>();

        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(logCostInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(exc);
    }
    /**
     * 解决 拦截器中注入bean 失败情况出现
     * addArgumentResolvers方法中 添加
     *  argumentResolvers.add(currentUserMethodArgumentResolver());
     */
    @Bean
    public LogCostInterceptor logCostInterceptor() {
        return new LogCostInterceptor();
    }

}