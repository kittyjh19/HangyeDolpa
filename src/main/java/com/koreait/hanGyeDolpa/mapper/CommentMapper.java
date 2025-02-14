package com.koreait.hanGyeDolpa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.hanGyeDolpa.bean.CommentVO;

@Mapper
public interface CommentMapper {
    List<CommentVO> getCommentsByBno(Long bno);
    //유저이름으로
    List<CommentVO> getCommentsByBnoandName(Long bno);
    boolean insertComment(CommentVO comment);
}