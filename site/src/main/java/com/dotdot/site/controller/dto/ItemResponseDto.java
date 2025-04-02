package com.dotdot.site.controller.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponseDto<T> {
    int status;
    T data;
}
