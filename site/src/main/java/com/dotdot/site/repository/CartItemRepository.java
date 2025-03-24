package com.dotdot.site.repository;

import com.dotdot.site.dto.CartDetailDto;
import com.dotdot.site.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> { // 장바구니에 들어갈 상품을 저장하거나 조회하기 위한 인터페이스

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // CartDetailDto 생성자를 이용하여 DTO를 반환할때 new com.shop.dto.CartDetailDto(...)
    @Query("SELECT new com.dotdot.site.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
                    "FROM CartItem ci, ItemImg im " +
                    "JOIN ci.item i " +
                    "WHERE ci.cart.id = :cartId " +
                    "AND im.item.id = ci.item.id " +
                    "AND im.repImgYn = 'Y' " +
                    "ORDER BY ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
