package com.koreait.hanGyeDolpa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MapService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String getPlaceID(String placeName, double Xposition, double Yposition) {
		
		String placeID = "http://place.map.kakao.com/";
		
		final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
		// 일단은 하드코딩 해놨는데, 나중에 서비스 배포시 반드시 암호화할 것
		final String KAKAO_API_KEY = "bba502ffca79940b6b67288936813ffd";

		// 요처URL 작성
		String getSearchKeyWord = placeName;
		String reqUrl = KAKAO_API_URL +
        		"?page=1" +
        		"&size=10" + //일단 동일값 10개 받기 -> 어케 처리할까
        		"&sort=accuracy" +
                "&query=" + getSearchKeyWord +
                "&y=" + Yposition +
                "&x=" + Xposition
                ;
				
		log.info("Request URL: {}", reqUrl);
		
		//헤더
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		
		//호출
		ResponseEntity<Map> response = restTemplate.exchange(reqUrl, HttpMethod.GET, entity, Map.class);
		log.info("Resp-> " + response.toString());
		
		try {
			// TODO - List형 말고 그냥 파싱해서 String으로 가져오고 싶다
			List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");
			
			if(documents != null && !documents.isEmpty()) {
//				if(documents.size() > 1) {
					String placeData = documents.stream()
				            .map(doc -> (String) doc.get("id"))
				            .filter(data -> data != null) // null 값 방지
				            .findFirst() // 첫 번째 값 반환
				            .orElse(null); // 값이 없을 경우 null 반환
					log.info("placeData: "+ placeData);
					// TODO 너무 별로인 코드. 좀 더 다듬기
					placeID+= placeData;
//				}
//				else {
					// TODO - 만약 이름이 중복이라면..?
//				}
			}
		}
		catch (ClassCastException e) {
            log.error("Failed to cast response documents to List<Map<String, Object>>", e);
        } catch (Exception e) {
            log.error("An error occurred while processing the response", e);
        }
		
		log.info("placeID: " + placeID);
		
		return placeID;
	}
}
