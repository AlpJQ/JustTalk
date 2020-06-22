package com.nowcoder.communityy.dao;

import com.nowcoder.communityy.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    // 根据实体显示评论信息，实体可以是帖子，课程，题目等
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    // 查询一共有多少条数据
    int selectCountByEntity(int entityType, int entityId);

    // 新增评论
    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}
