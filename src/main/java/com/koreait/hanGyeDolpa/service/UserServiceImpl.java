package com.koreait.hanGyeDolpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreait.hanGyeDolpa.bean.UserVO;
import com.koreait.hanGyeDolpa.mapper.UserMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper uMapper;
	
	@Override
	public boolean checkUserLogin(HttpSession session) {
		Long uNo = (Long) session.getAttribute("uNo");
		
		if(uNo == null || uNo == 0L) {
			if(uNo == null) {
				session.setAttribute("uNo", 0L);
				uNo = 0L;
			}
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public UserVO getUserDataAllByNo(Long userNo) {
		return uMapper.getUserDataAllByNo(userNo);
	}

	@Override
	public Long getUserNo(HttpSession session) {
		Long userNo = (Long) session.getAttribute("uNo");
		if(userNo == null) {
			userNo = 0L;
		}
		return userNo;
	}

}
