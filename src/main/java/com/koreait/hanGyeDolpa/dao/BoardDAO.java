package com.koreait.hanGyeDolpa.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.koreait.hanGyeDolpa.bean.BoardVO;
import com.koreait.hanGyeDolpa.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BoardDAO {
	
	@Autowired
	private BoardMapper mapper;
		
	// getList
	public List<BoardVO> getList() { return mapper.getList(); }
	public List<BoardVO> getListWithKey(String type, String keyword){
		return mapper.getListWithKey(type, keyword);
	}
	
	//사용자이름
	public List<BoardVO> getUserNameList(){
		return mapper.getUserNameList();
	}
	public List<BoardVO> getUserNameListWithKey(String type, String keyword){
		return mapper.getUserNameListWithKey(type, keyword);
	}
	
	// register
	public int register(BoardVO board) {
		int cnt  = mapper.insertSelectKey(board);
//		int cnt = mapper.insert(board);
		
		return cnt;
	}
	
	// read 
	public BoardVO read(Long bno) {
		return mapper.get(bno);
	}
	
	// modify
	public int modify(BoardVO board) {
		return mapper.update(board);
	}
	
	// remove
	public int remove(Long bno) {
		return mapper.delete(bno);
	}
	
	// viewcnt
	public void updateViewCount(Long bno) {
		mapper.updateViewCount(bno);
	}
	
	// 보드작성자번호
	public Long getUserIDinBoard(Long bno) {
		return mapper.getUserIDinBoard(bno);
	}
	
	// 읽을떄 사용자 이름 나오게
	public BoardVO getAllDataAndUserName(Long bno) {
		return mapper.getAllDataAndUserName(bno);
	}
	
}