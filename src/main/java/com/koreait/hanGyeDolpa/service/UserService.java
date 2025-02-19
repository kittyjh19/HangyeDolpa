package com.koreait.hanGyeDolpa.service;

import com.koreait.hanGyeDolpa.bean.UserVO;

import jakarta.servlet.http.HttpSession;

public interface UserService {
	public boolean checkUserLogin(HttpSession session);
	public Long getUserNo(HttpSession session);
	public UserVO getUserDataAllByNo(Long userNo);
}