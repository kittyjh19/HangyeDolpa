package com.koreait.hanGyeDolpa.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.koreait.hanGyeDolpa.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class BoardTest {

	@Autowired
	private BoardMapper bm;
	
//	@Test
	public void getAuthIDinBoard() {
		Long id = bm.getUserIDinBoard(5L);
		log.info("id->"+id);
	}
	
	@Test
	public void getUserNameListTest() {
		log.info(bm.getUserNameList().toString());
		
	}
}
