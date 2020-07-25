package com.fanfiction.DTO;

import com.fanfiction.models.Composition;
import com.fanfiction.models.User;

public class CommentDTO {
    private Long id;
    private User commentAuthor;
    private String text;
    private CompositionDTO compositionDTO;

    public CommentDTO(Long id, User commentAuthor, String text) {
        this.id = id;
        this.commentAuthor = commentAuthor;
        this.text = text;
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

    public CompositionDTO getCompositionDTO() {
        return compositionDTO;
    }

    public void setCompositionDTO(CompositionDTO compositionDTO) {
        this.compositionDTO = compositionDTO;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", commentAuthor=" + commentAuthor +
                ", text='" + text + '\'' +
                ", compositionDTO=" + compositionDTO +
                '}';
    }
}
