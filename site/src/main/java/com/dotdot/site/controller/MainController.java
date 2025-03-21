package com.dotdot.site.controller;

import com.dotdot.site.config.auth.PrincipalDetails;
import com.dotdot.site.dto.ItemSearchDto;
import com.dotdot.site.dto.MainItemDto;
import com.dotdot.site.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/main")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("principal", principal);

        return "shop/main";
    }
}
