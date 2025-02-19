package com.koreait.hanGyeDolpa.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.koreait.hanGyeDolpa.bean.UserVO;
import com.koreait.hanGyeDolpa.dto.KakaoTokenResponseDto;
import com.koreait.hanGyeDolpa.dto.KakaoUserInfoResponseDto;
import com.koreait.hanGyeDolpa.mapper.UserMapper;

import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl{
	
	@Autowired
	private final UserMapper userMapper;
	
    @Value("${kakao.client-id}")
    private String clientId;
    
    @Value("${kakao.client-id}")
    private String logoutUrl;

    @Value("${kakao.token-url:https://kauth.kakao.com}")
    private String KAUTH_TOKEN_URL_HOST;

    @Value("${kakao.user-url:https://kapi.kakao.com}")
    private String KAUTH_USER_URL_HOST;

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

//        log.info("Access Token: {}", kakaoTokenResponseDto.getAccessToken());
        return kakaoTokenResponseDto.getAccessToken();
    }
    
    public void getUserInfo(String accessToken, HttpSession session) {

        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();
        
        // 로그인 데이터 DB 관리용 함수 -> 뉴비? 기존?
        insertUserData2DB(userInfo, session);
    }
    
    // 로그아웃
    public void serviceLogout(String accessToken, HttpSession session) {
        final String reqUrl = "https://kauth.kakao.com/oauth/logout";

        try{
        	// 1. 전송 url 생성
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            // 1-1 의 응답은?
            int responseCode = conn.getResponseCode();
            log.info("카톡로그아웃 -> 응답 -> " + responseCode);

            // 2. 버퍼 읽기 -> 응답에 따라
            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            // 3. 결과 메시ㅣ지 만들기
            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("카톡 로갓 응답 결과: " + result);

        }
        // 오류처리
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // 로그인한 사용자 처리 함수
    private void insertUserData2DB(KakaoUserInfoResponseDto userInfo, HttpSession session) {
        
    	String authId = String.valueOf(userInfo.getId());
    	String userName = userInfo.getKakaoAccount().getProfile().getNickName();
    	String imgPath = userInfo.getKakaoAccount().getProfile().getProfileImageUrl();
    	
    	log.info("AuthID kakao -> " + authId);
    	
        // TODO 체크하기 ->
        // 매퍼에서 AuthId를 기반으로 동일한 사용자가 있는가?
        // if(true-이름일치) -> 기존의 user의 데이터가 전부 일치하나?(1. 프로필 2. 이름)
        // if(true-완전일치) -> AuthId를 기반으로 uNo 설정하기
        // else(false-안완전일치) -> AuthId를 기반으로 사용자 데이터 수정하기
        // else(false-새로운사람) -> insertUser(userDto) 하기
        
        // 1. AuthID로 사용자 있는지 확인
        Boolean userFlag = userMapper.checkDupData(authId);
        
        if(userFlag == null) { userFlag = false; }
        
        // 2. 중복사용자있나?
        	//2-1. 있음
        if(userFlag == true) {
    		//3. 변경사항 있나?
        	UserVO existUserVo = userMapper.getUserDataAll(authId);
        	checkUserDataIsEqual(existUserVo, userName, imgPath);
        }
        
        	//2-2.없음
        else {
        	// 신입 저장하기
        	UserVO userVO = new UserVO();
        	userVO.setAuthID(authId);;
        	userVO.setUserName(userName);
        	userVO.setUserProfilePath(imgPath);
            
            userMapper.insertUser(userVO);
        }
        
        // Session 저장
        session.setAttribute("uNo", userMapper.getUserNo(authId));
    }
    
    // 변경점 확인 -> 변경
    private void checkUserDataIsEqual(UserVO vo1, String userName, String imgPath) {
    	
    	// null 객체 거르기
    	if(vo1 == null) {
    		log.info("기존객체 없음! 로그인 데이터를 확인하셈요");
    	}
    	else {
	    		// 필드 비교
	    		// 1. 이름 || 프사
	    	if(vo1.getUserName().equals(userName) && vo1.getUserProfilePath().equals(imgPath)) {
	    		log.info("사용자 정보 변경 없음!");
	    	}
	    	else {
	    		log.info("사용자 정보 변경 있음! ------------ 변경합니다");
	    		userMapper.updateUserData(vo1.getAuthID(), userName, imgPath);
	    		// 변경사항 저장하기
	    	}
    	}
    }
}