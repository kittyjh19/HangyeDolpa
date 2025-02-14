package com.koreait.hanGyeDolpa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.hanGyeDolpa.bean.AttachFileVO;

@Mapper
public interface AttachFileMapper {
	public int insert(AttachFileVO vo);
	public List<AttachFileVO> findByBno(Long bno);
}