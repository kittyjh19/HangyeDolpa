package com.koreait.hanGyeDolpa.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userNo;
    private String authId;
    private String userName;
    private String userImg;
}