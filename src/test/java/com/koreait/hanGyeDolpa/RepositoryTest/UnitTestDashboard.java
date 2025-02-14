package com.koreait.hanGyeDolpa.RepositoryTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.koreait.hanGyeDolpa.repository.ExerciseRepository;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class UnitTestDashboard {

	@Autowired
	private ExerciseRepository exRepo;
//	
//	@Test
//	@DisplayName("캘린더그래프 녀석의 데이터 확인용")
//	void testCalendarData() {
//		List<checkDataForCalendar> result = exRepo.findDataForCalendar("2025-01-01", "2025-12-31");
//		
//		result.forEach(data -> log.info("============> Data: "+data));
//		int cnt = result.size();
//		
//		log.info("============> Data cnt: "+cnt);
//	}
	
	@Test
    @DisplayName("운동 시간 총합 데이터 확인")
    void testGetTotalExerciseTime() {
        // Given: DB에서 실제 데이터를 가져오기 위해 쿼리 실행
//        List<Exercise> exercises = exRepo.findAllByDateRange("2025-01-01", "2025-12-31");
//
//        //합산
//        Map<String, Integer> result = exercises.stream()
//                .collect(Collectors.groupingBy(
//                        exercise -> exercise.getExerciseDate().substring(0, 7), // "YYYY-MM" 형식 -> 월 기준 쪼개기
//                        Collectors.summingInt(Exercise::getClimbCount)
//                ));
//
//        result.forEach((date, totalTime) -> {
//        	if(date.equals("2025-01") || date.equals("2025-02")) {
//        		log.info("============> Date: " + date + " | Total Time: " + totalTime);
//        		}
//        	});
    }
}

