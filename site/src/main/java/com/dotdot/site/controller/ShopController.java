package com.dotdot.site.controller;

import com.dotdot.site.config.auth.PrincipalDetails;
import com.dotdot.site.model.Item;
import com.dotdot.site.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller // View 화면(*.html) 리턴
@RequestMapping("/shop")
public class ShopController {

    public final Logger log = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ItemService itemService;

    // 쇼핑몰 메인 페이지
    @GetMapping ("/main")
    public String shopMain(Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        model.addAttribute("principal", principal);

        return "views/shop/shopMain";
    }

    @GetMapping("/createItem")
    public String createItem(Model model,  @AuthenticationPrincipal PrincipalDetails principal) {
        int newId = itemService.nextId();
        model.addAttribute("principal", principal);
        model.addAttribute("id", newId);

        return "views/shop/item/createItem";
    }
}
