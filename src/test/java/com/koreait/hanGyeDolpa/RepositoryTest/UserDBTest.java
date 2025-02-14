package com.koreait.hanGyeDolpa.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.koreait.hanGyeDolpa.bean.UserVO;
import com.koreait.hanGyeDolpa.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UserDBTest {

	@Autowired
	private UserMapper us;
	
//	@Autowired
//	private UserDto udto;
	
//	@Test
	public void getUserNOTEST() {
		Long val = us.getUserNo("3907973882");
		
		log.info("------------- > 번호 -> " + val);
	}
	
//	@Test
	public void getUserDataFromNO() {
		UserVO uv = us.getUserData(4L);
		log.info("------------- > 유저정보 -> " +uv.toString());
	}
	
//	@Test
	public void checkDupDataTest() {
		Boolean flag = us.checkDupData("3918463028");
		if(flag == null) {
			flag = false;
		}
		log.info("------------- > 중복유저 -> " + flag);
	}
	
	@Test
	public void getUserDataAll() {
		UserVO uv = us.getUserDataAll("3907973882");
		log.info("------------- > 유저정보 -> " +uv.toString());
	}
}
