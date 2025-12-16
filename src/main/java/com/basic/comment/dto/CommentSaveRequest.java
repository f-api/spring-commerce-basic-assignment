package com.basic.comment.dto;

import lombok.Getter;

@Getter
public class CommentSaveRequest {

    private String content;
    private String author;
    private String password;
}
