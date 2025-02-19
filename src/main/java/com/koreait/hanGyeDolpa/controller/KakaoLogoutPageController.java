package com.koreait.hanGyeDolpa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/logout/*")
@Slf4j
public class KakaoLogoutPageController {

	@Value("${kakao.client-id}")
    private String client_id;

    @Value("${kakao.logout.redirect-uri}")
    private String redirect_uri;

    @GetMapping("page")
    public String logOutPage(Model model, HttpSession session) {
        String location = "https://kauth.kakao.com/oauth/logout?client_id="+client_id+"&logout_redirect_uri="+redirect_uri;
        model.addAttribute("location", location);
        
        String akey = session.getAttribute("aT").toString();
        
		// Session끝 -> 무효화
		session.setAttribute("uNo", 0L);
		Long uNo = (Long) session.getAttribute("uNo");
		
		log.info("로그아웃이 완료되었습니다 -> 유저번회: " + uNo);
        
        return "Sample_kakaoLogout";
    }
}