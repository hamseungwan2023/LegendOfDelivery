package com.sparta.legendofdelivery.domain.dibs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreUserDto {
    private String storeName;
    private String userName;
}
