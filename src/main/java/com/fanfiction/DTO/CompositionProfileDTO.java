package com.fanfiction.DTO;

public class CompositionProfileDTO {
    private Long compositionId;
    private String title;
    private String description;
    private int chaptersAmount;

    public CompositionProfileDTO(Long compositionId, String title, String description, int chaptersAmount) {
        this.compositionId = compositionId;
        this.title = title;
        this.description = description;
        this.chaptersAmount = chaptersAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Long compositionId) {
        this.compositionId = compositionId;
    }

    public int getChaptersAmount() {
        return chaptersAmount;
    }

    public void setChaptersAmount(int chaptersAmount) {
        this.chaptersAmount = chaptersAmount;
    }
}
