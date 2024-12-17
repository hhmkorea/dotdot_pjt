package com.dotdot.site.service;

import com.dotdot.site.model.Member;
import com.dotdot.site.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

//    @Autowired // DI가 되어 주입됨.
//    private BCryptPasswordEncoder encoder;

    public Member login(final String username) { //, final String password) {

        // 1. 회원 정보 및 비밀번호 조회
        Member member = memberRepository.findAllByUsername(username);
        // String encodedPassword = (member == null) ? "" : member.getPassword();

        // 2. 회원 정보 및 비밀번호 체크
//        if (member == null || passwordEncoder.matches(password, encodedPassword) == false) {
//            return null;
//        }

        // 3. 회원 응답 객체에서 비밀번호를 제거한 후 회원 정보 리턴
        member.clearPassword();

        return member;
    }

    @Transactional
    public int saveMember(Member member) {
        //member.encodingPassword(passwordEncoder);   // password 인코딩
        memberRepository.save(member);
        return member.getId();
    }

    public int countMemberByUsername(final String username) {
        return memberRepository.countByUsername(username);
    }

}
