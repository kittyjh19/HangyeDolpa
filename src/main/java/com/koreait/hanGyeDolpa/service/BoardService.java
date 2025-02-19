package com.koreait.hanGyeDolpa.service;

import java.util.List;

import org.springframework.ui.Model;

import com.koreait.hanGyeDolpa.bean.AttachFileVO;
import com.koreait.hanGyeDolpa.bean.BoardVO;
import com.koreait.hanGyeDolpa.bean.CommentVO;

import jakarta.servlet.http.HttpSession;

public interface BoardService {
	public boolean checkUserRight(Long bNo, HttpSession session);// 유저 확인
	
	public Model readBoardService(Long bno, Model model);
	public String makeRegiMsg(BoardVO vo);
	public String makeModifyMsg(BoardVO vo, HttpSession session);
	public String makeDeleteMsg(Long bNo, HttpSession session);
	public String makeCommentMsg(CommentVO comment);
	
	public List<BoardVO> getUserNameList();
	public List<BoardVO> getUserNameListWithKey(String type, String keyword);
	
	public void updateViewCount(Long bno);
	public List<AttachFileVO> getAttachFileList(Long bno);


}
