package com.koreait.hanGyeDolpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.koreait.hanGyeDolpa.entity.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
	List<Exercise> findByUserNoAndExerciseDate(Long userNo, String exerciseDate);
	
	Exercise findByExerciseDate(String exerciseDate);

	// 전체 데이터 전부 다(날짜기준)
	@Query("SELECT e FROM Exercise e WHERE e.exerciseDate BETWEEN :startDate AND :endDate AND e.userNo = :userNo")
	List<Exercise> findAllByDateRange(
		@Param("userNo") Long userNo,
	    @Param("startDate") String startDate,
	    @Param("endDate") String endDate
	);
}