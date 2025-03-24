package com.dotdot.site.controller;

import com.dotdot.site.dto.CartDetailDto;
import com.dotdot.site.dto.CartItemDto;
import com.dotdot.site.dto.CartOrderDto;
import com.dotdot.site.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class CartController { // 장바구니 관련 요청 처리.

    private final CartService cartService;

    @PostMapping(value = "/cart") // 상품을 장바구니에 담는 로직 호출
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) { // 장바구니에 담을 상품 정보를 받는 cartItemDto 객체에 에러 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName(); // 현재 로그인한 회원의 이메일 정보
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email); // 장바구니에 상품 담기
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value = "/cart") // 장바구니 상품 상세 정보를 보여주는 로직
    public String orderHist(Principal principal, Model model) {

        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName()); // 현재 로그인한 사용자 의 장바구니 상품 정보 조회

        model.addAttribute("cartItems", cartDetailList); // 조회한 장바구니 상품 정보 뷰로 전달.

        return "views/shop/cart/cartList";
    }

    @PatchMapping(value = "/cartItem/{cartItemId}") // 장바구니 상품 정보 수정하는 로직
    // PatchMapping : Http 메소드에서 Patch된 자원의 일부를 업데이트할 경우 사용 --- 여기서는 장바구니 상품의 수량만 업데이트
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {

        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count); // 장바구니 개수 업데이트
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {

        if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if (cartOrderDtoList == null || cartOrderDtoList.isEmpty()) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDto orderDto : cartOrderDtoList) {
            if (!cartService.validateCartItem(orderDto.getCartItemId(), principal.getName())) {
                return new ResponseEntity<String>("주문할 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName()); // 장바구니 상품 주문 로직 호출

        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}
