package com.koreait.hanGyeDolpa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.hanGyeDolpa.bean.UserVO;

@Mapper
public interface UserMapper {
    void insertUser(UserVO userVO);
    Long getUserNo(String authId);
    UserVO getUserData(Long userNo);
    Boolean checkDupData(String authId);
    UserVO getUserDataAll(String authId);
    UserVO getUserDataAllByNo(Long userNo);
    void updateUserData(String authId, String userName, String userProfilePath);
}