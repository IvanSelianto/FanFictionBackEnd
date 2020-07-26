package com.fanfiction.DTO;

public class ChapterDTO {
    private String name;
    private String text;
    private Long id;
    private String imgUrl;
    private Long compositionId;
    private int chapterNumber;

    public ChapterDTO(String name, String text, Long id, String imgUrl, Long compositionId, int chapterNumber) {
        this.name = name;
        this.text = text;
        this.id = id;
        this.imgUrl = imgUrl;
        this.compositionId = compositionId;
        this.chapterNumber = chapterNumber;
    }

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
