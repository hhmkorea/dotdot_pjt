package com.dotdot.site.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    final Path FILE_ROOT = Paths.get("./").toAbsolutePath().normalize();    // resources 폴더가 아닌 위치 일때, 현재 위치를 지정함.
    private final String uploadPath = FILE_ROOT.toString() + "/site/upload/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String connectPath = "/uploadPath/**";
        registry.addResourceHandler(connectPath)                                // src 에 들어갈 경로
                .addResourceLocations("file:" + uploadPath)                    // 파일 경로
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

}

// 소스 출처 : https://greed-yb.tistory.com/253