package com.koreait.hanGyeDolpa.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.koreait.hanGyeDolpa.bean.AttachFileVO;
import com.koreait.hanGyeDolpa.mapper.AttachFileMapper;

@Repository
public class AttachFileDAO {

	@Autowired
	private AttachFileMapper AFMapper;
	
	public int insertBoardFile(AttachFileVO vo) {
		return AFMapper.insert(vo);
	}
	
	// 첨부파일 읽기
	public List<AttachFileVO> getAttachList(Long bno){
		return AFMapper.findByBno(bno);
	}
}
