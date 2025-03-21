package com.dotdot.site.model;

import com.dotdot.site.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order by 키워드가 있어 orders로 명칭 지정.
@Getter
@Setter
public class Order extends BaseEntity { // 주문 객체

    @Id
    @Column(name = "order_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // OrderItem과 양방향 참조 관계
    // OneToMany : 일대다 매핑,
    // mappedBy : OrderItem 엔티티가 연관 관계 주인. OrderItem에 있는 Order에 의해 관리된다는 의미.
    // cascade : 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이
    // orphanRemoval : 고아 객체 제거 여부
    private List<OrderItem> orderItems = new ArrayList<OrderItem>(); // 하나의 주문이 여러 개의 주문 상품을 가지므로 List 자료형 사용해서 매핑.

    public void addOrderItem(OrderItem orderItem) { // 주문 상품 담기.
        orderItems.add(orderItem);
        orderItem.setOrder(this); // Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계, OrderItem 객체에도 Order 객체 셋팅.
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) { // 주문 생성
        Order order = new Order();
        order.setMember(member); // 상품 정보 중 회원 정보에 주문한 회원 정보로 셋팅

        for (OrderItem orderItem : orderItemList) { // 장바구니 페이지에는 한 번에 여러 개의 상품을 주문할 수 있음.
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now()); // 현재 시간을 주문시간으로 설정.

        return order;
    }

    public int getTotalPrice() { // 총 주문 금액
        int totalPrice = 0;

        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL; // 주문 취소

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 재고 더해줌
        }
    }
}
