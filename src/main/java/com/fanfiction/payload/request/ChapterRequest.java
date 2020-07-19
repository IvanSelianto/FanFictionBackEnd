package com.fanfiction.payload.request;

public class ChapterRequest {
    private String imgUrl;
    private String text;
    private String name;
    private Long compositionId;
    private Long id;
    private int chapterNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Long compositionId) {
        this.compositionId = compositionId;
    }

    @Override
    public String toString() {
        return "ChapterRequest{" +
                "imgUrl='" + imgUrl + '\'' +
                ", text='" + text + '\'' +
                ", name='" + name + '\'' +
                ", compositionId=" + compositionId +
                ", id=" + id +
                ", chapterNumber=" + chapterNumber +
                '}';
    }
}
