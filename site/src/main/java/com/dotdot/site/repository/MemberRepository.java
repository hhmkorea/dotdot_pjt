package com.dotdot.site.repository;

import com.dotdot.site.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    // SELECT * FROM user WHERE username = 1?;
    Member findAllByUsername(String username);
    Member findByUsername(String username);
    int countByUsername(String username);
}
