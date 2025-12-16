package com.basic.comment.service;

import com.basic.comment.dto.CommentSaveRequest;
import com.basic.comment.dto.CommentSaveResponse;
import com.basic.comment.entity.Comment;
import com.basic.comment.repository.CommentRepository;
import com.basic.schedule.entity.Schedule;
import com.basic.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CommentSaveResponse save(Long scheduleId, CommentSaveRequest request) {
        if (request.getContent() == null) {
            throw new IllegalStateException("댓글의 제목은 필수값입니다.");
        }
        if (request.getPassword() == null) {
            throw new IllegalStateException("비밀번호는 필수값입니다.");
        }
        if (request.getAuthor() == null) {
            throw new IllegalStateException("작성자명은 필수값입니다.");
        }
        if (request.getContent().length() > 200) {
            throw new IllegalStateException("댓글의 내용은 최대 길이는 100자입니다.");
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 일정입니다.")
        );
        Long commentCount = commentRepository.countByScheduleId(schedule.getId());

        // 댓글이 10개 이상일 때
        if (commentCount > 10) {
            throw new IllegalStateException("댓글은 최대 10개까지만 작성할 수 있습니다.");
        }

        // 댓글 작성이 가능할 때
        Comment comment = new Comment(
                schedule.getId(),
                request.getContent(),
                request.getAuthor(),
                request.getPassword()
        );
        Comment savedComment = commentRepository.save(comment);
        return new CommentSaveResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getAuthor(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }
}
