package com.dotdot.site.service;

import com.dotdot.site.model.Member;
import com.dotdot.site.model.RoleType;
import com.dotdot.site.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public int saveMember(Member member) {
        String rawPassword = member.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        member.setPassword(encPassword);
        member.setRole(RoleType.USER);
        memberRepository.save(member);
        return member.getId();
    }

    public int countMemberByUsername(final String username) {
        return memberRepository.countByUsername(username);
    }

}
