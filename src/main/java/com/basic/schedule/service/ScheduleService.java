package com.basic.schedule.service;

import com.basic.comment.dto.CommentGetOneResponse;
import com.basic.comment.entity.Comment;
import com.basic.comment.repository.CommentRepository;
import com.basic.schedule.dto.*;
import com.basic.schedule.entity.Schedule;
import com.basic.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ScheduleSaveResponse save(ScheduleSaveRequest request) {
        if (request.getTitle() == null) {
            throw new IllegalStateException("일정의 제목은 필수값입니다.");
        }
        if (request.getTitle().length() > 30) {
            throw new IllegalStateException("일정의 제목은 최대 길이는 30자입니다.");
        }
        if (request.getPassword() == null) {
            throw new IllegalStateException("비밀번호는 필수값입니다.");
        }
        if (request.getContent() == null) {
            throw new IllegalStateException("일정의 내용은 필수값입니다.");
        }
        if (request.getAuthor() == null) {
            throw new IllegalStateException("작성자명은 필수값입니다.");
        }
        if (request.getContent().length() > 200) {
            throw new IllegalStateException("일정의 내용은 최대 길이는 200자입니다.");
        }

        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getAuthor(),
                request.getPassword()
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleSaveResponse(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getAuthor(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ScheduleGetAllResponse> findAll(String author) {
        // 작성자가 존재하지 않을 때
        if (author == null) {
            List<Schedule> schedules = scheduleRepository.findAllByOrderByModifiedAt();
            return schedules.stream()
                    .map(schedule -> new ScheduleGetAllResponse(
                            schedule.getId(),
                            schedule.getTitle(),
                            schedule.getContent(),
                            schedule.getAuthor(),
                            schedule.getCreatedAt(),
                            schedule.getModifiedAt()
                    )).toList();
        }

        // 작성자가 존재할 때
        List<Schedule> schedulesByAuthor = scheduleRepository.findAllByAuthorOrderByModifiedAt(author);
        List<ScheduleGetAllResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedulesByAuthor) {
            dtos.add(
                    new ScheduleGetAllResponse(
                            schedule.getId(),
                            schedule.getTitle(),
                            schedule.getContent(),
                            schedule.getAuthor(),
                            schedule.getCreatedAt(),
                            schedule.getModifiedAt()
                    )
            );
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public ScheduleGetOneResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 일정입니다.")
        );

        List<Comment> comments = commentRepository.findAllByScheduleId(schedule.getId());
        List<CommentGetOneResponse> commentDtos = comments.stream()
                .map(comment -> new CommentGetOneResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getAuthor()
                )).toList();

        return new ScheduleGetOneResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor(),
                commentDtos,
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional
    public ScheduleUpdateResponse updateTitleAndAuthor(
            Long scheduleId,
            ScheduleUpdateRequest request
    ) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 일정입니다.")
        );

        // 비밀번호가 같지 않을 때
        if (!ObjectUtils.nullSafeEquals(request.getPassword(), schedule.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호가 같을 때
        schedule.updateTitleAndAuthor(
                request.getTitle(),
                request.getAuthor()
        );
        return new ScheduleUpdateResponse(
                schedule.getId(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    @Transactional
    public void delete(Long scheduleId, String password) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 일정입니다.")
        );

        if (!ObjectUtils.nullSafeEquals(password, schedule.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.deleteById(schedule.getId());
    }
}
