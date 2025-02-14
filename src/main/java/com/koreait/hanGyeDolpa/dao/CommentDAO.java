package com.koreait.hanGyeDolpa.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.koreait.hanGyeDolpa.bean.CommentVO;
import com.koreait.hanGyeDolpa.mapper.CommentMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CommentDAO {
	@Autowired
    private CommentMapper commentMapper;

    public List<CommentVO> getCommentsByBno(Long bno) {
        return commentMapper.getCommentsByBno(bno);
    }

    public boolean addComment(CommentVO comment) {
        return commentMapper.insertComment(comment);
    }
    
    public List<CommentVO> getCommentsByBnoandName(Long bno) {
        return commentMapper.getCommentsByBnoandName(bno);
    }
}