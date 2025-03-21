package com.dotdot.site.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{ // 주문 상품 객체

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 <-> 즉시로딩 FetchType.EAGER
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩, Order와 양방향 참조 관계
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격

    private int count; // 수량

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item); // 주문할 상품
        orderItem.setCount(count); // 주문 수량
        orderItem.setOrderPrice(item.getPrice()); // 현재 시간 기준으로 상품 가격을 주문 가격으로 셋팅.

        item.removeStock(count); // 주문 수량만큼 상품의 재고 수량 감소

        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count; // 해당 상품을 주문한 총 가격
    }

    public void cancel() {
        this.getItem().addStock(count); // 주문 취소시 주문 수량만큼 재상품 재고 더해줌.
    }
}
