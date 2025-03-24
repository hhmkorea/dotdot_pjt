package com.dotdot.site.service;

import com.dotdot.site.dto.ItemFromDto;
import com.dotdot.site.dto.ItemImgDto;
import com.dotdot.site.dto.ItemSearchDto;
import com.dotdot.site.dto.MainItemDto;
import com.dotdot.site.model.Item;
import com.dotdot.site.model.ItemImg;
import com.dotdot.site.repository.ItemImgRepository;
import com.dotdot.site.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService { // 상품 관련 로직

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFromDto itemFromDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = itemFromDto.createItem(); // 상품 등록 폼으로 입력 받은 데이터 이용, item 객체 생성
        itemRepository.save(item); // 상품 데이터 저장

        // 이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0) { // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 셋팅
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품 이미지 정보 저장.
        }

        return item.getId();
    }

    @Transactional(readOnly = true) // 읽기 전용, JPA가 더티체킹(변경감지)를 수행하지 않아서 성능 향상.
    public ItemFromDto getItemDtl(Long itemId) {

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품 이미지 오름차순 목록 조회

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) { // 조회한ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가.
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new); // 상품의 아이디를 통해 상품 엔티티 조회.
        ItemFromDto itemFromDto = ItemFromDto.of(item);
        itemFromDto.setItemImgDtoList(itemImgDtoList);

        return itemFromDto;
    }

    public Long updateItem(ItemFromDto itemFromDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 수정
        Item item = itemRepository.findById(itemFromDto.getId()).orElseThrow(EntityNotFoundException::new); // 상품 등록 화면으로 부터 전달받은 상품 아이디를 이용하여 상품 엔티티 조회
        item.updateItem(itemFromDto);

        List<Long> itemImgIds = itemFromDto.getItemImgIds(); // 상품 이미지 리스트 조회

        // 이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i)); // 상품 이미지 업데이트
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
