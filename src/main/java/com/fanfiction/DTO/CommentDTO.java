package com.fanfiction.DTO;

import com.fanfiction.models.Composition;
import com.fanfiction.models.User;

public class CommentDTO {
    private Long id;
    private User commentAuthor;
    private String text;
    private Composition composition;

    public CommentDTO(Long id, User commentAuthor, String text, Composition composition) {
        this.id = id;
        this.commentAuthor = commentAuthor;
        this.text = text;
        this.composition = composition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(User commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }
}
