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
@Data
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 1, max = 50)
    @NotBlank(message = "제목은 필수 입력입니다.")
    @Column(nullable = false, length = 50)
    private String title;

    @NotBlank(message = "내용은 필수 입력 입니다.")
    @Column(columnDefinition = "longblob")
    private String content;

    private int viewCnt; // 조회수

    // FetchType.EAGER : 해당 Entity(테이블) 조인해서 데이타 다 가져옴.
    // FetchType.LAZY : 해당 Entity(테이블) 조인해서 "필요하면" 데이타 가져올게!, 예:펼치기
    @ManyToOne(fetch = FetchType.EAGER) // 연관관계, Board = Many, Member = One
    @JoinColumn(name = "memberId")
    private Member member; // 작성자

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
