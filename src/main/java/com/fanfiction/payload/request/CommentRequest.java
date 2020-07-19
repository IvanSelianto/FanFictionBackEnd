package com.fanfiction.payload.request;

import com.fanfiction.models.Composition;

public class CommentRequest {
   private String text;
   private Composition composition;

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

    @Override
    public String toString() {
        return "CommentRequest{" +
                "text='" + text + '\'' +
                ", composition=" + composition +
                '}';
    }
}
