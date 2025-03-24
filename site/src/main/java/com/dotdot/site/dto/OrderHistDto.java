package com.dotdot.site.dto;

import com.dotdot.site.constant.OrderStatus;
import com.dotdot.site.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto { // 주문 정보

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); // 주문 날짜
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto) { // OrderItemDto 객체를 주문 상품 리스트에 추가
        orderItemDtoList.add(orderItemDto);
    }
}
