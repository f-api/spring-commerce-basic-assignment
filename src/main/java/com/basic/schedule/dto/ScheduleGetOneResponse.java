package com.basic.schedule.dto;

import com.basic.comment.dto.CommentGetOneResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleGetOneResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final List<CommentGetOneResponse> comments;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ScheduleGetOneResponse(
            Long id,
            String title,
            String content,
            String author,
            List<CommentGetOneResponse> comments,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.comments = comments;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
