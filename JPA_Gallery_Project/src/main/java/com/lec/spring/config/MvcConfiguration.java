package com.lec.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 파일 업로드 관련
// resource 경로 설정
@Configuration
public class MvcConfiguration {

    @Configuration
    public static class LocalMvcConfiguration implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            System.out.println("\tLocalMvcConfiguration.addResourceHandlers() 호출");

            registry
                    .addResourceHandler("/upload/**")
                    .addResourceLocations("file:upload/")
            ;
        }
    }

}












