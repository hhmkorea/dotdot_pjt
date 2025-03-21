package com.dotdot.site.service;

import com.dotdot.site.dto.OrderDto;
import com.dotdot.site.dto.OrderHistDto;
import com.dotdot.site.dto.OrderItemDto;
import com.dotdot.site.model.*;
import com.dotdot.site.repository.ItemImgRepository;
import com.dotdot.site.repository.ItemRepository;
import com.dotdot.site.repository.MemberRepository;
import com.dotdot.site.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService { // 주문 관련 로직

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) { // 주문 하기
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new); // 주문할 상품 조회.
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원의 이메일 정보로 회원정보 조회.

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문 상품 엔티티 생성.
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList); // 주문 엔티티 생성.
        orderRepository.save(order); // 주문 엔티티 저장.

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderHist(String email, Pageable pageable) { // 주문 목록 조회

        List<Order> orders = orderRepository.findOrders(email, pageable); // 유저 주문 목록
        Long totalCount = orderRepository.countOrder(email); // 유저 주문 총 개수

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        for (Order order : orders) { // 구매 이력 페이지에 전달할 DTO 생성
            OrderHistDto orderHistDto = new OrderHistDto(order);

            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y"); // 주문한 상품 대표 이미지 조회
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtoList.add(orderHistDto);
        }

        return new PageImpl<>(orderHistDtoList, pageable, totalCount); // 페이지 구현 객체를 생성하여 반환
    }

    @Transactional(readOnly = true)
    public boolean validationOrder(Long orderId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) { // 현재 로그인한 사용자와 주문 데이터 생성자가 같은지 검사.
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) { // 주문 취소
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder(); // 주문 취소 상태로 변경, 변경 감지 기능에 의해 트랜잭션 끝날 때 update 쿼리 실행
    }

    public Long orders(List<OrderDto> orderDtoList, String email) { // 장바구니 상품들 주문 하기 

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) { // 주문할 상품 리스트 
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); 
            orderItemList.add(orderItem); 
        }

        Order order = Order.createOrder(member, orderItemList); // 주문 엔티티 만들기
        orderRepository.save(order); // 주문 데이터 저장

        return order.getId();
    }
}
