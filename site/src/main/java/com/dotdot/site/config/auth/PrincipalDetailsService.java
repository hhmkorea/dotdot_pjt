package com.dotdot.site.config.auth;

import com.dotdot.site.model.Member;
import com.dotdot.site.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login --> 스프링 시큐리티 기본 로그인 요청 주소 /login => 여기서 동작을 안한다.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    /* 스프링이 요그인 요청을 가로챌 때, username, password 변수 2개를 가로챔
       password 부분 처리는 알아서 함, username이 DB에 있는지만 확인해주면 됨. */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername()");
        Member principal = memberRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username);
        });
        System.out.println("principal: " + principal);
        return new PrincipalDetails(principal); // Security의 세션에 유저 정보가 저장됨.
    }
}
