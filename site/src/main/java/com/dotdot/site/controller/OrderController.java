package com.dotdot.site.controller;

import com.dotdot.site.config.auth.PrincipalDetails;
import com.dotdot.site.dto.OrderDto;
import com.dotdot.site.dto.OrderHistDto;
import com.dotdot.site.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class OrderController { // 주문 관련 요청 처리, 비동기 방식(웹 페이지 새로 고침 없이 서버에 주문 요청)

    private final OrderService orderService;

    @PostMapping(value = "/order") // 주문 로직 호출
    public @ResponseBody ResponseEntity order (@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal) {
        /* 스프링 비동기 처리
        @RequestBody : Http 요청의 본문 body에 담긴 내용을 자바객체로 전달.
        @ResponseBody : 자바 객체를 Http 요청의 body로 전달.
        */

        if (bindingResult.hasErrors()) { // 주문 정보 받는 orderDto 객체 에러 체크.
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getUsername(); // 세션에 담긴 현재 로그인 유저 정보.
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email); // 주문 로직 호출
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK); // 주문 성공 응답 코드 반환
    }

    @GetMapping(value = {"/orders", "/orders/{page}"}) // 구매 이력 로직 호출
    public String orderHist(@PathVariable("page") Optional<Integer> page, @AuthenticationPrincipal PrincipalDetails principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4); // 한 번에 가지고 올 주문 개수는 총 4개

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderHist(principal.getUsername(), pageable); // 현재 로그인한 회원의 주문 목록 데이터

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "views/shop/order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, @AuthenticationPrincipal PrincipalDetails principal) {

        if (!orderService.validationOrder(orderId, principal.getUsername())) { // 주문 취소 권한 검사
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId); // 주문 취소 호출

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
