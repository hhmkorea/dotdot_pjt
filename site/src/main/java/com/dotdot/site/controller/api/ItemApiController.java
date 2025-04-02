package com.dotdot.site.controller.api;

import com.dotdot.site.config.auth.PrincipalDetails;
import com.dotdot.site.controller.dto.ItemResponseDto;
import com.dotdot.site.model.Item;
import com.dotdot.site.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ItemApiController {

    @Autowired
    private ItemService itemService;

    // 상품 등록
    @PostMapping("/items/new")
    public ItemResponseDto<Integer> createItem(@Valid @RequestBody Item item, @AuthenticationPrincipal PrincipalDetails principal){
        itemService.saveItem(item, principal.getMember());
        return new ItemResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
