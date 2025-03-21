package com.dotdot.site.dto;

import com.dotdot.site.model.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String orgImgName;

    private String imgUrl;

    private String regImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); // 멤버 변수로 ModelMapper 객체를 추가

    public static ItemImgDto of(ItemImg itemImg) { // static 메소드로 선언, 객체 성성없이도 호출 가능.
        return modelMapper.map(itemImg, ItemImgDto.class); // ItemImg 엔티티 객체를 파라미터로 받음 이름 같으면 ItemImgDto로 값 복사해 반환
    }
}
