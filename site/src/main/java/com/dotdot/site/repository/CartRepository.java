package com.dotdot.site.repository;

import com.dotdot.site.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByMemberId(int memberId); // 현재 로그인한 회원의 Cart 엔티티 찾기.
}
