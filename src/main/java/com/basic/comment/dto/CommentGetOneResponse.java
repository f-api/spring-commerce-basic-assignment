package com.basic.comment.dto;

import lombok.Getter;

@Getter
public class CommentGetOneResponse {

    private final Long id;
    private final String content;
    private final String author;

    public CommentGetOneResponse(Long id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }
}
