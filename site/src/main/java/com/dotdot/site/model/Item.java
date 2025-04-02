package com.dotdot.site.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 1, max = 50)
    @NotBlank(message = "상품명은 필수 입력입니다.")
    @Column(nullable = false, length = 50)
    private String name;
    private int price;

    private int stockQuantity; // 상품 수량

    @NotBlank(message = "상품 상세 내용은 필수 입력 입니다.")
    @Column(columnDefinition = "longblob")
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member; // 등록자

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
