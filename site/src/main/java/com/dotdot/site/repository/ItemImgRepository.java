package com.dotdot.site.repository;

import com.dotdot.site.model.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId); // 상품 아이디로 상품 이미지 아이디의 오름차순으로 상품 목록 가져옴.

    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn); // 상품 대표 이미지 찾기
}
