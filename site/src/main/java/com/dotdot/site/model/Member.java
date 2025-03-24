package com.dotdot.site.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, max = 20)
    @NotBlank(message = "사용자id는 필수 입력입니다.")
    //@Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "3~20자리의 숫자 또는 문자만 가능합니다.")
    @Column(nullable = false, length = 20)
    private String username;

    @NotBlank(message = "패스워드는 필수 입력입니다.")
    //@Pattern(regexp = "^.*(?=^.{4,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "비밀번호는 4~15자리의 숫자,문자,특수문자로 이루어져야합니다.")
    @Column(nullable = false, length = 100) // 해쉬(비밀번호 암호화) 대비
    private String password;

//    @Column(nullable = false, length = 50)
//    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role; // USER, ADMIN ---> security 최신 버전에서는 권한 적용시 ROLE_ 쓰지 않음.

    @CreationTimestamp
    private LocalDateTime createdDate;

    public void clearPassword() {
        this.password = "";
    }

}
