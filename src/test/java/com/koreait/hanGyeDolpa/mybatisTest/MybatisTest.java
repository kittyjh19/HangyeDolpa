package com.koreait.hanGyeDolpa.mybatisTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.koreait.hanGyeDolpa.bean.BoardVO;
import com.koreait.hanGyeDolpa.dao.BoardDAO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MybatisTest {

	@Autowired
	private BoardDAO dao;
	
	@Test
	public void test1() {
		log.info("======123");
		
		BoardVO vo = dao.read(1);
		
		log.info("======456");
		
		log.info("------------------------------" + vo);
		
	}
	
}
