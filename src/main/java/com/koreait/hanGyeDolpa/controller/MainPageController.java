package com.koreait.hanGyeDolpa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreait.hanGyeDolpa.bean.UserVO;
import com.koreait.hanGyeDolpa.mapper.UserMapper;
import com.koreait.hanGyeDolpa.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainPageController {
	
	boolean loginFlag = false;
	
	@Autowired
	private UserService uService;
	
	@GetMapping("/mainHeaderCheckUserLogin")
	@ResponseBody
	public Map<String, Object> mainHeaderCheckUserLogin(HttpSession session) {
		
		Map<String, Object> resp = new HashMap<>();
		
		// Session 에서 슈킹
		Long uNo = uService.getUserNo(session);
		
		if(uNo == 0) {
//			log.info("~~~~~~~~~~~~~~~~ "+uNo);
			resp.put("uVO", null);
		}
		else {
		// uno로 가져오기
	//		UserDto udt = uMap.get
			//TODO uno기반 슈킹
			UserVO uv = uService.getUserDataAllByNo(uNo);
			
			resp.put("uVO", uv);
			
		}
		resp.put("uNo", uNo);
		return resp;
	}
	
	@GetMapping("communityTest")
	public String communityTest() {
		return "Sample_Community.html";
	}
	
	@GetMapping("loginTest")
	public String loginTest() {
		return "Sample_Login.html";
	}
	
	@GetMapping("dashBoardTest")
	public String dashBoardTest() {
		return "dashboard.html";
	}
	
	@GetMapping("userProfile")
	public String userProfileTest() {
		return "maintenancePage.html";
	}

	@GetMapping("eventPage")
	public String eventPage() {
		return "maintenancePage.html";
	}

}
