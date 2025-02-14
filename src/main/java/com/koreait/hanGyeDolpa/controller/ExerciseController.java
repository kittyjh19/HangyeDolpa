package com.koreait.hanGyeDolpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koreait.hanGyeDolpa.dto.ExerciseRecordRequest;
import com.koreait.hanGyeDolpa.dto.ExerciseRequest;
import com.koreait.hanGyeDolpa.entity.Exercise;
import com.koreait.hanGyeDolpa.repository.ExerciseRepository;
import com.koreait.hanGyeDolpa.service.ExerciseService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/exercise")
@Slf4j
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseService eService;
    
    @PostMapping("/save")
    public String saveExerciseRecord(
    									@RequestBody ExerciseRecordRequest request
    									){
        // 컨트롤러 부분은 비즈니스 로직이 없는게 권장됩니다.(SOLID SRP/OCP/DIP)
    	// 따라서 아래 내용은 전부 서비스 단으로 옮기겠습니다.
    	eService.saveExerciseData(request);
        
        return "redirect:/dashboard";
    }
    
    @PostMapping("/records")
    public ResponseEntity<List<Exercise>> getRecords(@RequestBody ExerciseRequest request) {
        // 요청에서 날짜와 사용자 ID를 가져옴
    	String exerciseDate = request.getExerciseDate();
        Long userId = request.getUserId();

        log.info(""+userId+ "---"+exerciseDate);

        List<Exercise> records = exerciseRepository.findByUserNoAndExerciseDate(userId, exerciseDate);
        log.info(""+records);
        return ResponseEntity.ok(records);
    }
    
    
}
