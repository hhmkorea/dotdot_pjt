package com.dotdot.site.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig {
    public final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    /* 로그인 실패 핸들러 의존성 주입 */
    private final AuthenticationFailureHandler customnFailureHandler;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable); // csrf 비활성화, @EnableWebSecurity 이 csrf 공격을 방지하기 때문에 해당 기능을 disable로 변경
        http
                .addFilter(corsConfig.corsFilter()) // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O) --> 모든 요청 허용.
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/logout", "/auth/**", "/admin/**","/shop/**", "/api/**", "/js/**", "/css/**", "/images/**", "/icons/**", "/uploadPath/**").permitAll() // 해당 경로는 권한이 없어도 접속할 수 있다
                        .requestMatchers("/").hasRole("USER")              // 권한체크 - 내부적으로 ROLE_ 을 붙이기 때문에 ROLE_ 뒷부분만 적어준다
                        .requestMatchers("/shop/admin/**").hasRole("ADMIN")     // 권한체크 - 내부적으로 ROLE_ 을 붙이기 때문에 ROLE_ 뒷부분만 적어준다
                        .anyRequest().permitAll() // 그리고 나머지 url은 전부 권한을 허용해준다.
                );

        http.formLogin(f -> f.loginPage("/auth/loginForm") // 로그인 처리 프로세스 설정
                .loginProcessingUrl("/auth/loginProc")
                .failureHandler(customnFailureHandler) // 로그인 실패 핸들러
                .defaultSuccessUrl("/home")
        );

        http.logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/")
                // 로그아웃 핸들러 추가 (세션 무효화 처리)
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    session.invalidate();
                })
                // 로그아웃 성공 핸들러 추가 (리다이렉션 처리)
                .logoutSuccessHandler((request, response, authentication) ->
                    response.sendRedirect("/"))
                .deleteCookies("JSESSIONID", "access_token"));
        return http.build();
    }
}
