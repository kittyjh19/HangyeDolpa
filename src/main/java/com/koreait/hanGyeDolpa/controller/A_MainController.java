package com.koreait.hanGyeDolpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.hanGyeDolpa.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class A_MainController {

	@Autowired
	private UserService uService;
	
	@GetMapping("/")
	public String headerTest(Long userNo, HttpSession session) {
		boolean flag = uService.checkUserLogin(session);
		
		if(!flag) {
			session.setAttribute("uNo", 0L);
		}
		
		return "mainPage/headerTest";
	}
	
	@GetMapping("/dashBoard")
	public String dashBoardTest(HttpSession session, RedirectAttributes rttr) {
		boolean flag = uService.checkUserLogin(session);
		
		if(!flag) {
			rttr.addFlashAttribute("msg", "로그인 정보가 없습니다!\n로그인 페이지로 이동합니다.");
			return "redirect:/login/page";
		}
		else {
			return "dashboard.html";
		}
	}
	
	@GetMapping("/mapLocation")
	public String mapLocation() {
		return "mapLocation/mapLocation";
	}
	
	@GetMapping("aboutService")
	public String aboutService() {
		return "aboutService.html";
	}
	
	 @GetMapping("/exercise")
	 public String exercise() {
	     return "exercise";
	 }
	 
	 @GetMapping("/exercise/add")
	 public String addExercise() {
	     return "add-exercise";
	 }

	 @GetMapping("/about")
	 public String addAbout(){
		return "aboutService"; 
	 }
}