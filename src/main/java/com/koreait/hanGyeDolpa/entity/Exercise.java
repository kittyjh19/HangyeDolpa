package com.koreait.hanGyeDolpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "EXERCISE_TABLE")
public class Exercise {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIMB_NO_SEQ")
    @SequenceGenerator(name = "CLIMB_NO_SEQ", sequenceName = "CLIMB_NO_SEQ", allocationSize = 1)
    @Column(name = "CLIMB_NO")
    private Long climbNo;

    
    @Column(name = "USER_NO", nullable = false)
    private Long userNo;

    @Column(name = "CLIMB_PLACE", nullable = false)
    private String climbPlace;

    @Column(name = "CLIMB_STAGE", nullable = false)
    private Integer climbStage;

    @Column(name = "CLIMB_COUNT", nullable = false)
    private Integer climbCount;

    @Column(name = "CLIMB_TIME", nullable = false)
    private Integer climbTime; // Stored in minutes
    
    @Column(name = "CLIMB_DATE", nullable = false)
    private String exerciseDate;

    @Column(name = "CLIMB_KCAL", nullable = false)
    private Integer climbKcal;


}
