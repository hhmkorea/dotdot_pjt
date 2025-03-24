package com.dotdot.site.model;

import com.dotdot.site.constant.ItemSellStatus;
import com.dotdot.site.dto.ItemFromDto;
import com.dotdot.site.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity { // 상품 객체

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;            // 상품코드

    @Column(nullable = false, length = 50)
    private String itemNm;      // 상품명

    @Column(name = "price", nullable = false)
    private int price;          // 가격

    @Column(nullable = false)
    private int stockNumber;    // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

/*
    @ManyToMany // 다대다 맵핑, 실무에서 사용하지 않고 연결 테이블을 생성해서 일대다, 다대일 관계로 풀어냄.
    @JoinTable(
            name = "member_item",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Member> members;
*/

    public void updateItem(ItemFromDto itemFromDto) { // 상품 업데이트 로직
        this.itemNm = itemFromDto.getItemNm();
        this.price = itemFromDto.getPrice();
        this.stockNumber = itemFromDto.getStockNumber();
        this.itemDetail = itemFromDto.getItemDetail();
        this.itemSellStatus = itemFromDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) { // 상품 재고 감소
        int restStock = this.stockNumber - stockNumber; // 상품 재고 수량 - 주문 수량 = 남은 재고 수량

        if (restStock < 0) { // 상품 재고 수량이 주문 수량보다 작을 경우 재고 부족 예외 발생.
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량 : " + stockNumber + ")");
        }

        this.stockNumber = restStock; // 주문 후 남은 재고 수량 상품의 현재 재고 값으로 할당.
    }

    public void addStock(int stockNumber) { // 상품 재고 증가
        this.stockNumber += stockNumber;
    }
}
