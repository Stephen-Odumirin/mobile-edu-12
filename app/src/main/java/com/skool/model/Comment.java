package com.skool.model;

public class Comment {
    private  String name;
    private String commentBody;

    public Comment(String name, String commentBody) {
        this.name = name;
        this.commentBody = commentBody;
    }

    public String getName() {
        return name;
    }

    public String getCommentBody() {
        return commentBody;
    }
}
