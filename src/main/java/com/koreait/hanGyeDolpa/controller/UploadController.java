package com.koreait.hanGyeDolpa.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.koreait.hanGyeDolpa.bean.AttachFileVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/upload/*")
public class UploadController {
	
	@PostMapping(value="uploadAjaxAction", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // 메서드 종료 시 html(view)로 가지 않고 데이터를 리턴
	public List<AttachFileVO> uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("[uploadController] uploadAjaxPost() Called OK");
		
		List<AttachFileVO> fileList = new ArrayList();
		
		// tmp 경로로 설정하기(시스템마다 다름.)
		String uploadFolder = [본인 경로];
		String uploadFolderPath = getFolder();
		// yyyy/mm/dd 경로 생
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		
		// 디렉토리가 없으면 생성 / 있으면 skip
		if(uploadPath.exists()) {
			log.info("이미 디렉토리가 존재");
			
		}
		else {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile f : uploadFile) {
			log.info("filename : " + f.getOriginalFilename());
			log.info("filesize : " + f.getSize());
			
			// UUID 적용
			// Network 상에서 각각의 개체를 식별하기 위해 사용 
			String uploadFileName = f.getOriginalFilename();
			UUID uuid = UUID.randomUUID();
			log.info("uuid : " + uuid.toString());
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			AttachFileVO attachFileVO = new AttachFileVO();
			attachFileVO.setFileName(uploadFileName);
			attachFileVO.setUuid(uuid.toString());
			attachFileVO.setUploadPath(uploadFolderPath);
			
			
			// 1. File Creation(Empty)
			File saveFile = new File(uploadPath, uploadFileName);
			
			// 2. Contents Copy
			try {
				f.transferTo(saveFile);
				
				if(checkImageType(saveFile)) {
					log.info("파일 저장 완료 -> 업로드 컨트롤러");
					attachFileVO.setImage(true);
				}
				if (checkVideoType(saveFile)) {
					log.info("비디오 저장 완료 -> 업로드 컨트롤러");
	                attachFileVO.setVid(true);
	            }
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			fileList.add(attachFileVO);
		}
		log.info("리스트 저장 완료 -> 업로드 컨트롤러");
		return fileList;
	}
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			log.info("contentType : " + contentType);
			return contentType.startsWith("image");
		}
		catch(Exception e) { e.printStackTrace(); }
		
		return false;
	}
	// 동영상 여부 확인 메서드 추가
	private boolean checkVideoType(File file) {
	    try {
	        String contentType = Files.probeContentType(file.toPath());
	        return contentType != null && contentType.startsWith("video");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// 오늘 일자를 연/월/일 형태로 리턴한다.
	private String getFolder() {
		String str = null;
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
		str = sdf.format(date);
		
		return str;
	}
	
	// display 기능 추가**
	@GetMapping("display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
//	    log.info("------> 디스플레이 호출~ " + fileName);

	    ResponseEntity<byte[]> result = null;
	    HttpHeaders header = new HttpHeaders();

	    File file = new File([본인경로], fileName);

	    try {
	        if (!file.exists()) {  // 파일이 존재하지 않으면 404 반환
	            log.warn("File not found: " + file.getAbsolutePath());
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        header.add("Content-Type", Files.probeContentType(file.toPath()));
	        result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}
}