package com.koreait.hanGyeDolpa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koreait.hanGyeDolpa.dto.checkDataForCalendar;
import com.koreait.hanGyeDolpa.service.DashboardService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class DashboardRestController {

	@Autowired
	private DashboardService dashbService;
	
	@GetMapping("/getCalendarData")
	public List<checkDataForCalendar> getCalendarData(
										@RequestParam String startDate,
										@RequestParam String endDate,
										HttpSession session
										){
		
		Long userNo = (Long) session.getAttribute("uNo");
		
		return dashbService.getCalendarData(startDate, endDate, userNo);
	}
	
	@GetMapping("/getComboChartData")
	public ResponseEntity<Map<String, Map<Integer, Integer>>> getComboChartData(
										@RequestParam String startDate,
										@RequestParam String endDate,
										HttpSession session
										){
		Long userNo = getUserNoInHttpSession(session);
		Map<String, Map<Integer, Integer>> totalValue = dashbService.getComboData(startDate, endDate, userNo);
		
		if(totalValue == null || totalValue.isEmpty()) {
		}
		
		return ResponseEntity.ok(totalValue);
	}
	
	@GetMapping("/getTotalTimeData")
	public Map<String, Map<String, Integer>> getTotalTimeData(
										@RequestParam String startDate,
										@RequestParam String endDate,
										HttpSession session
										){
		Long userNo = getUserNoInHttpSession(session);
		return dashbService.getTotlaData(startDate, endDate, userNo);
		
	}
	
	@GetMapping("/getHighstScoreData")
	public Map<String, Integer> getHighstScoreData(
										@RequestParam String startDate,
										@RequestParam String endDate,
										HttpSession session
										){
		Long userNo = getUserNoInHttpSession(session);
		return dashbService.getHighstScore(startDate, endDate, userNo);
	}
	
	@GetMapping("/setSessionStorage")
	public String setSessionStorage(HttpSession session) {
		
		// 서버 -> 클라이어느 세션
	    Long uNoValue = getUserNoInHttpSession(session);
	    return uNoValue != null ? uNoValue.toString() : "";
	}
	
	private Long getUserNoInHttpSession(HttpSession session) {
		
		Long userNo = (Long) session.getAttribute("uNo");
		
		return userNo;
	}
}
