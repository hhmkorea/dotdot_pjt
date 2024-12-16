package com.dotdot.site.model;

import jakarta.persistence.*;
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
@Table(name = "tb_board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "longblob")
    private String content;

    private int viewcnt; // 조회수

    // FetchType.EAGER : 해당 Entity(테이블) 조인해서 데이타 다 가져옴.
    // FetchType.LAZY : 해당 Entity(테이블) 조인해서 "필요하면" 데이타 가져올게!, 예:펼치기
    @ManyToOne(fetch = FetchType.EAGER) // 연관관계, Board = Many, User = One
    @JoinColumn(name = "userId")
    private Member member; // 작성자

    @CreationTimestamp
    private LocalDateTime creatDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
