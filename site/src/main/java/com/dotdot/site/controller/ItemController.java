package com.dotdot.site.controller;

import com.dotdot.site.config.auth.PrincipalDetails;
import com.dotdot.site.dto.ItemFromDto;
import com.dotdot.site.dto.ItemSearchDto;
import com.dotdot.site.model.Item;
import com.dotdot.site.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        model.addAttribute("itemFromDto", new ItemFromDto());
        model.addAttribute("principal", principal);

        return "views/shop/item/itemForm"; // 상품 페이지
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFromDto itemFromDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList, @AuthenticationPrincipal PrincipalDetails principal ) {

        model.addAttribute("principal", principal);

        if (bindingResult.hasErrors()) { // 상품 등록시 필수 값이 없다면.. 
            return "views/shop/item/itemForm"; // 상품 등록 페이지로 전환
        }

        if (itemImgFileList.get(0).isEmpty() && itemFromDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "views/shop/item/itemForm";
        }

        try {
            itemService.saveItem(itemFromDto, itemImgFileList); // 상품 저장
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "views/shop/item/itemForm";
        }

        return "redirect:/shop/main"; // 메인으로 이동.
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemAdminDtl(@PathVariable("itemId") Long itemId, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        model.addAttribute("principal", principal);

        try {
            ItemFromDto itemFromDto = itemService.getItemDtl(itemId); // 조회한 상품 데이터를 모델에 담아서 뷰로 전달.
            model.addAttribute("itemFromDto", itemFromDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFromDto", new ItemFromDto());
            return "views/shop/item/itemForm";
        }

        return "views/shop/item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFromDto itemFromDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        model.addAttribute("principal", principal);

        if (bindingResult.hasErrors()) {
            return "views/shop/item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFromDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "views/shop/item/itemForm";
        }

        try {
            itemService.updateItem(itemFromDto, itemImgFileList); // 상품 수정
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "views/shop/item/itemForm";
        }

        return "redirect:/shop/main";
    }

    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); // 조회할 페이지 번호, 한번에 가지고 올 데이터 수

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable); // 조회조건과 페이지 정보를 넘겨서 Page<Item> 객체 반환 받음.
        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이지 정보 뷰에 전달.
        model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환시 기존 검색 조건 유지한채 이돌 할 수 있도록 뷰에 다시 전달.
        model.addAttribute("maxPage", 5); // 상품 관리 하단에 보여줄 페이지 번호의 최대 개수.
        model.addAttribute("principal", principal);
        
        return "views/shop/item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        ItemFromDto itemFromDto = itemService.getItemDtl(itemId);
        model.addAttribute("principal", principal);
        model.addAttribute("item", itemFromDto);
        return "views/shop/item/itemDtl";
    }
}
