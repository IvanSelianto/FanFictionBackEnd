package com.fanfiction.payload.request;

import java.util.HashSet;
import java.util.Set;

public class CompositionRequest {
    private String title;
    private String description;
    private Set<String> compositionGenres = new HashSet<>();
private Long compositionId;

    public Long getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Long compositionId) {
        this.compositionId = compositionId;
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

    public Set<String> getCompositionGenres() {
        return compositionGenres;
    }

    public void setCompositionGenres(Set<String> compositionGenres) {
        this.compositionGenres = compositionGenres;
    }
}
