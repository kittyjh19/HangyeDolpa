package com.koreait.hanGyeDolpa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.koreait.hanGyeDolpa.bean.AttachFileVO;
import com.koreait.hanGyeDolpa.bean.BoardVO;
import com.koreait.hanGyeDolpa.bean.CommentVO;
import com.koreait.hanGyeDolpa.dao.AttachFileDAO;
import com.koreait.hanGyeDolpa.dao.BoardDAO;
import com.koreait.hanGyeDolpa.dao.CommentDAO;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardDAO bDao;
	
	@Autowired
	private CommentDAO cDao;
	
	@Autowired
	private AttachFileDAO afDao;
	
	private Long getBoardWriterId(Long bNo) {
		return bDao.getUserIDinBoard(bNo);
	}
	
	private Long getLoginUserId(HttpSession session) {
		Long userNo = (Long) session.getAttribute("uNo");
		if(userNo == null) {
			userNo = 0L;
		}
		return userNo;
	}
	
	private Map<String, Integer> boardRegister(BoardVO vo){
		
		int cntFile = 0;
		int cntBoard = (int)bDao.register(vo);
		
		Map<String, Integer> boardRegiResultMap = new HashMap<>();
		boardRegiResultMap.put("글", cntBoard);
		
		// 파일이 있으면
		if(vo.getAttachFile() != null) {
			for(AttachFileVO af : vo.getAttachFile()) {
				Long bno = vo.getBno();
				af.setBno(bno);
				afDao.insertBoardFile(af);
				cntFile++;
			}
		}
		boardRegiResultMap.put("파일", cntFile);
		
		return boardRegiResultMap;
	}
	
	private BoardVO boardRead(Long bno) {
		return bDao.getAllDataAndUserName(bno);
	}
	
	private int boardModify(BoardVO vo){
		return bDao.modify(vo);
	}
	
	private int boardRemove(Long bNo) {
		return bDao.remove(bNo);
	}
	
	@Override
	public boolean checkUserRight(Long bNo, HttpSession session) {
		boolean flag = false;
		Long logInUser = getLoginUserId(session);
		Long writerUser = getBoardWriterId(bNo);
		
		if(logInUser != null) {
			if(logInUser.equals(writerUser)) {
			flag = true;
			}
		}
		return flag;
	}
	
	@Override
	public String makeRegiMsg(BoardVO vo) {
		Map<String, Integer> msgMap = boardRegister(vo);
		
		String msg = "글 등록 ";
		int bcnt = msgMap.get("글");
		int afcnt = msgMap.get("파일");
		
		if(bcnt > 0) {
			msg += "완료!\n글: ";
			msg += bcnt + "건, 파일: " + afcnt+ "건";
		}
		else {
			msg += "글 등록 없음";
		}
		
		return msg;
	}
	
	@Override
	public String makeModifyMsg(BoardVO vo, HttpSession session) {
		
//		boolean flag = checkUserRight(vo.getBno(), session);

		String msg = "글 수정: ";
		msg += boardModify(vo) > 0 ? "성공" : "실패";
		
//		if(flag) {
//			
//		}
//		else {
//			msg += "작성자 본인만 수정 가능합니다~\n로그인 정보를 확인하세요";
//		}

		return msg;
	}
	
	@Override
	public String makeDeleteMsg(Long bNo, HttpSession session) {
//		boolean flag = checkUserRight(bNo, session);

		String msg = "글삭제: ";
		msg += boardRemove(bNo) > 0 ? "성공" : "실패";
		
		// 확인 완료 후 지우기 -> 진짜 다른 사람이 접속했을 때 글 지우기 버튼 안보이는지
//		if(flag) {
//			
//		}
//		else {
//			msg += "작성자 본인만 삭제 가능합니다~\n로그인 정보를 확인하세요";
//		}
//		
		return msg;
	}
	
	@Override
	public String makeCommentMsg(CommentVO comment) {
		String msg = "등록 ";
		
		boolean cmtFlag = cDao.addComment(comment);
		
		msg += cmtFlag ? "성공" : "실패";
		msg += "했습니다.";
		
		return msg;
	}
	
	@Override
	public Model readBoardService(Long bno, Model model) {
		model.addAttribute("board", boardRead(bno));

		List<CommentVO> cvo = cDao.getCommentsByBnoandName(bno);
		model.addAttribute("comments", cvo);
				
		return model;
	}
	
	@Override
	public List<AttachFileVO> getAttachFileList(Long bno){
		List<AttachFileVO> afv = afDao.getAttachList(bno);
		return afv;
	}

	@Override
	public void updateViewCount(Long bno) {
		bDao.updateViewCount(bno);
	}

	@Override
	public List<BoardVO> getUserNameList() {
		return bDao.getUserNameList();
	}

	@Override
	public List<BoardVO> getUserNameListWithKey(String type, String keyword) {
		return bDao.getUserNameListWithKey(type, keyword);
	}
}
