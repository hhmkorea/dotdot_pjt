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

    // TO-DO : 패스워드 암호화

    @Transactional
    public int saveMember(Member member) {
        //member.encodingPassword(passwordEncoder);   // password 인코딩
        memberRepository.save(member);
        return member.getId();
    }

}
