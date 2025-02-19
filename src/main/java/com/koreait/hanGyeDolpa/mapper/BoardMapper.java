package com.koreait.hanGyeDolpa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.hanGyeDolpa.bean.BoardVO;

@Mapper
public interface BoardMapper {
	//게시글 추가
	public int insert(BoardVO vo);
	public int insertSelectKey(BoardVO vo); // no 채번 결과를 vo에 담음
	
	// 게시글 리스트
	public List<BoardVO> getList();
	public List<BoardVO> getListWithKey(String type, String keyword);
	
	// 게시글리스트2 -> 유저 번호 대신 유저 이름 출력하기
	public List<BoardVO> getUserNameList();
	public List<BoardVO> getUserNameListWithKey(String type, String keyword);
	
	// 게시글 조회
	public BoardVO get(Long bno);
	
	//조회2 -> 유저 번호 대신 유저 이름
	public BoardVO getAllDataAndUserName(Long bno);
	
	// 게시글 업데이트
	public int update(BoardVO vo);
	
	// 게시글 삭제
	public int delete(Long bno);
	
	// 조회수 증가
	public void updateViewCount(Long bno);
	
	// 글번호를 기준으로 userId 가져오기
	public Long getUserIDinBoard(Long bno);
}
