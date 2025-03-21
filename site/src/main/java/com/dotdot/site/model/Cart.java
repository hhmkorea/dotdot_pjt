package com.dotdot.site.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity { // 장바구니 객체

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // 일대일 매핑, 지연로딩
    @JoinColumn(name = "member_id") // 매핑할 FK
    private Member member;

    public static Cart createCart(Member member) { // 처음 장바구니 담을 때 장바구니 생성.
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
