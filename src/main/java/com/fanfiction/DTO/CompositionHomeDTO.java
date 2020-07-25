package com.fanfiction.DTO;

import com.fanfiction.models.Genre;

import java.util.HashSet;
import java.util.Set;

public class CompositionHomeDTO {
    private Long id;
    private String title;
    private String description;
    private Set<Genre> compositionGenres = new HashSet<>();
    private String publicationDate;

    public Set<Genre> getCompositionGenres() {
        return compositionGenres;
    }

    public void setCompositionGenres(Set<Genre> compositionGenres) {
        this.compositionGenres = compositionGenres;
    }

    public CompositionHomeDTO(Long id, String title, String description, Set<Genre> compositionGenres, String publicationDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.compositionGenres = compositionGenres;
        this.publicationDate = publicationDate;
    }

    public CompositionHomeDTO(String title, String description, String publicationDate) {
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
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


    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
