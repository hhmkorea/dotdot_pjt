package com.dotdot.site.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_img")
@Getter
@Setter
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; // 이미지 파일명

    private String orgImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조히 경로

    private String repImgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY) // 상품 엔티티와 1:N 단방향 매핑.
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String orgImgName, String imgName, String imgUrl) { // 이미지 정보 업데이트하는 메소드
        this.orgImgName = orgImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
