package com.dotdot.site.repository;

import com.dotdot.site.dto.ItemSearchDto;
import com.dotdot.site.dto.MainItemDto;
import com.dotdot.site.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
