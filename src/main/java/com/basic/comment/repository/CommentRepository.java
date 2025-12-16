package com.basic.comment.repository;

import com.basic.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByScheduleId(Long id);

    List<Comment> findAllByScheduleId(Long id);
}
