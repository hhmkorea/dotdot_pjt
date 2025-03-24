package com.dotdot.site.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity { // 장바구니 상품 객체

    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 매핑, 지연로딩
    @JoinColumn(name = "cart_id") // 매핑할 FK
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count; // 같은 상품을 몇 개 담을지 저장.

    public static CartItem createCartItem(Cart cart, Item item, int count) { // 장바구니에 담을 상품 엔티티 생성
        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count) { // 장바구니에 담을 상품 수량 증가
        this.count += count;
    }

    public void updateCount(int count) { // 장바구니에 담긴 상품 수량 변경 
        this.count = count;
    }
}
