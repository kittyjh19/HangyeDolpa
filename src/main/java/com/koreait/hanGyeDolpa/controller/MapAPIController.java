package com.koreait.hanGyeDolpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreait.hanGyeDolpa.service.MapService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MapAPIController {
	
	@Autowired
	private MapService mService;
	
	// 마커 누르면 호출
	@ResponseBody
	@GetMapping("/clickMarker")
	public String clickMarker(String placeName, double Xposition, double Yposition) {
		
		String placeUrl = mService.getPlaceID(placeName, Xposition, Yposition);
		
		return placeUrl;
	}
	
}
