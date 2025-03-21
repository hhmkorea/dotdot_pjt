package com.dotdot.site.service;

import com.dotdot.site.dto.CartDetailDto;
import com.dotdot.site.dto.CartItemDto;
import com.dotdot.site.dto.CartOrderDto;
import com.dotdot.site.dto.OrderDto;
import com.dotdot.site.model.Cart;
import com.dotdot.site.model.CartItem;
import com.dotdot.site.model.Item;
import com.dotdot.site.model.Member;
import com.dotdot.site.repository.CartItemRepository;
import com.dotdot.site.repository.CartRepository;
import com.dotdot.site.repository.ItemRepository;
import com.dotdot.site.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService { // 장바구니 관련 로직

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email) {

        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new); // 장바구나에 담을 상품 엔티티 조회.
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원 엔티티 조회.

        Cart cart = cartRepository.findByMemberId(member.getId()); // 현재 로그인한 회원의 장바구니 엔티티 조회.

        if (cart == null) { // 상품을 처음으로 장바구니에 담을 경우 장바구니 엔티티 생성.
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());  // 현재 상품이 이미 들어가 있는지 조회.

        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount()); // 장바구니에 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수량 만큼 더해줌.
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount()); // CartItem 엔티티 저장.
            cartItemRepository.save(cartItem); // 장바구니에 들어갈 상품 저장.
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) { // 현재 로그인한 회원의 장바구니에 들어있는 상품 조회

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) { // 장바구니가 없는 경우 빈 리스트
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId()); // 장바구니에 담겨있는 상품 정보 조회.

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email); // 현재 로그인한 회원 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member saveMember = cartItem.getCart().getMember(); // 장바구니 상품을 저장한 회원 조회

        if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) { // 회원 정보 같은지 확인
            return false;
        }

        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count) { // 장바구니 상품의 수량을 업데이트
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) { // 장바구니 상품 삭제
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) { // 주문 로직으로 전달할 orderDto 객체를 만듬
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());

            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email); // 주문 로직 호출.

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            cartItemRepository.delete(cartItem); // 주문한 상풀들 장바구니에서 제거
        }

        return orderId;
    }
}
