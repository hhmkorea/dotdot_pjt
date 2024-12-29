package com.dotdot.site.config.auth;

import com.dotdot.site.model.Member;
import com.dotdot.site.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login --> 스프링 시큐리티 기본 로그인 요청 주소 /login => 여기서 동작을 안한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername()");
        Member userEntity = userRepository.findByUsername(username);
        System.out.println("userEntity: " + userEntity);
        return new PrincipalDetails(userEntity);
    }
}
