package com.dotdot.site.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA Auditing 기능을 활성화
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() { // 등록자와 수정자를 처리해주는 AuditorAware를 빈으로 등록
        return new AuditorAwareImpl();
    }
}
