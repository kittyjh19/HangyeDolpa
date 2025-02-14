package com.koreait.hanGyeDolpa.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreait.hanGyeDolpa.bean.AttachFileVO;
import com.koreait.hanGyeDolpa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board/*")
@Slf4j
public class BoardController {

	@Autowired
	private BoardService bService;

	// List
	@GetMapping("list")
	public void list(String type, String keyword, Model model) {
		log.info("Type -> " + type + " -> Keyword -> "+ keyword);
		if(keyword == null) {
			// 사용자 이름
			model.addAttribute("list", bService.getUserNameList());
		}
		else {
			// 사용자 이름
			model.addAttribute("list",bService.getUserNameListWithKey(type, keyword));
		}
		
	}
	
	// localhost:10000/board/read?=N을 호출했을 때 내용을 보여주는 페이지
	@RequestMapping("read")
	public void read(Long bno, Model model) {
		// 조회수 증가
		bService.updateViewCount(bno);
		// 게시글 정보 가져오기
		model.addAttribute(bService.readBoardService(bno, model));
	}
	
	@GetMapping(value="/getAttachList", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AttachFileVO> getAttachList(Long bno){
		return bService.getAttachFileList(bno);
	}
	
}
