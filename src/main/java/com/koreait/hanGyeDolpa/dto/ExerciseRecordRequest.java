package com.koreait.hanGyeDolpa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseRecordRequest {
	private Long userId;
	private String exerciseDate;
	
    private String exercisePlace;
    private Integer exerciseStage;
    private Integer exerciseCount;
    private Integer exerciseTime;
    private Integer exerciseKcal;
    
    }
