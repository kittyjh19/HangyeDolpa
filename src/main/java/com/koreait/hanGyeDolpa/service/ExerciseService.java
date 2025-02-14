package com.koreait.hanGyeDolpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreait.hanGyeDolpa.dto.ExerciseRecordRequest;
import com.koreait.hanGyeDolpa.entity.Exercise;
import com.koreait.hanGyeDolpa.repository.ExerciseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExerciseService {
    
	@Autowired
    private ExerciseRepository eRepo;
    
	public boolean saveExerciseData(ExerciseRecordRequest request) {
	    
		try {
	        Exercise record = new Exercise();
	        record.setUserNo(request.getUserId());
	        record.setClimbPlace(request.getExercisePlace());
	        record.setClimbStage(request.getExerciseStage());
	        record.setClimbCount(request.getExerciseCount());
	        record.setClimbTime(request.getExerciseTime());
	        record.setExerciseDate(request.getExerciseDate());
	        record.setClimbKcal(request.getExerciseKcal());

	        Exercise savedRecord = eRepo.save(record);
	        
	        // 성공 여부 확인
	        return savedRecord != null;  

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
