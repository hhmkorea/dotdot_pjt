package com.dotdot.site.service;

import com.dotdot.site.model.ItemImg;
import com.dotdot.site.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService { // 상품 이미지 관련 로직

    @Value("${file.itemImgLocation}") // application.yml 에 설정한 itemImgLocation 프로퍼티 값을 읽어옴.
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String orgImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(orgImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, orgImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName; // /shop/item 폴더에 이미지를 저장하므로 상품 이미지 불러오는 경로로 /images/item/ 으로 붙여줌.
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(orgImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) { // 상품 이미지를 수정한 경우 상품 이미지 업데이트
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String orgImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, orgImgName, itemImgFile.getBytes()); // 업데이트 한 상품 이미지 파일 업로드.
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(orgImgName, imgName, imgUrl); // 변경한 상품 이미지 정보 셋팅.
            // itemImgRepository.save() 호출 안함.
            // savedItemImg 엔티티는 현재 [영속 상태]이므로 데이터를 변경하는 것만으로 '변경 감지 기능' 동작 --> 트랜잭션 끝날 때 update 쿼리 실행.
        }
    }
}
