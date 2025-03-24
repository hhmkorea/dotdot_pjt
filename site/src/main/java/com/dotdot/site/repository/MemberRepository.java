package com.dotdot.site.repository;

import com.dotdot.site.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    // SELECT * FROM user WHERE username = 1?;
    Optional<Member> findByUsername(String username);
    int countByUsername(String username);

    Member findByEmail(String email); // 회원 가입 시 중복된 회원 이메일로 검사.

}
