package com.koreait.hanGyeDolpa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login/*")
public class KakaoLoginPageController {
	
	@Value("${kakao.client-id}")
    private String client_id;

    @Value("${kakao.redirect-uri}")
    private String redirect_uri;

    @GetMapping("page")
    public String loginPage(Model model, RedirectAttributes rttr) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        model.addAttribute("location", location);

        // 걍 바로 로긴 페이지로 오면
        if(rttr.getFlashAttributes().isEmpty()) {
        	rttr.addFlashAttribute("msg", "");
        }
        
        return "Sample_kakaoLogin";
    }
}
