package com.fanfiction.DTO;

import com.fanfiction.models.User;

import java.util.Objects;
import java.util.Set;

public class CompositionDTO {
    private Long compositionId;
    private String title;
    private String description;
    private Set<GenresNewCompositionDTO> compositionGenres;
    private User author;

    public CompositionDTO(User author, String title, String description, Set<GenresNewCompositionDTO> compositionGenres, Long compositionId) {
        this.compositionId = compositionId;
        this.title = title;
        this.description = description;
        this.compositionGenres = compositionGenres;
        this.author = author;
    }


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

    public Set<GenresNewCompositionDTO> getCompositionGenres() {
        return compositionGenres;
    }

    public void setCompositionGenres(Set<GenresNewCompositionDTO> compositionGenres) {
        this.compositionGenres = compositionGenres;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "CompositionDTO{" +
                "compositionId=" + compositionId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", compositionGenres=" + compositionGenres +
                ", author=" + author +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CompositionDTO that = (CompositionDTO) object;
        return Objects.equals(compositionId, that.compositionId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(compositionGenres, that.compositionGenres) &&
                Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compositionId, title, description, compositionGenres, author);
    }
}
