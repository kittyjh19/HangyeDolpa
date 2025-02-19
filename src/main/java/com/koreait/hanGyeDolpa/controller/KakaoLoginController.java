package com.koreait.hanGyeDolpa.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koreait.hanGyeDolpa.service.LoginServiceImpl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

	@Autowired
	private final LoginServiceImpl kakaoService;

    @GetMapping("/callbacks")
    public ResponseEntity<Void> callback(@RequestParam("code") String code, HttpSession session) throws IOException {
        
    	// 엑세스 토큰 콜백 관련 -> 3단계 진행 후 메인페이지로 넘김
    	String accessToken = kakaoService.getAccessTokenFromKakao(code);
    	session.setAttribute("aT", accessToken);
    	
    	
        // TODO -> 만약 오류가 난다면?
        kakaoService.getUserInfo(accessToken, session);
        
        // TODO -> 
        	//if(오류) -> 뭔가 안된 alert + 메인페이지(uNo => 0)
        	//else(true) -> 성공 alert + 메인페이지(uNo => uNo)
    	
        //응답 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
        
//        return "redirect:/";
    }
    
    @GetMapping("/logoutCallback")
    public ResponseEntity<Void> logoutCallback(HttpSession session) {
        
//    	// 엑세스 토큰 콜백 관련 -> 3단계 진행 후 메인페이지로 넘김
//    	String accessToken = kakaoService.getAccessTokenFromKakao(code);
//    	session.setAttribute("aT", accessToken);
//    	
//        // TODO -> 만약 오류가 난다면?
//        kakaoService.getUserInfo(accessToken, session);
//        
//        // TODO -> 
//        	//if(오류) -> 뭔가 안된 alert + 메인페이지(uNo => 0)
//        	//else(true) -> 성공 alert + 메인페이지(uNo => uNo)
//    	
        //응답 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}