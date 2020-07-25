package com.fanfiction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Indexed
@Table(name = "compositions")
public class Composition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Field
    @Size(max = 510)
    private String description;
    @Field
    @Size(max = 100)
    private String title;
    @ManyToOne
    private User author;
    @ManyToMany
    private Set<Genre> compositionGenres = new HashSet<>();
    @Column(columnDefinition = "DATETIME")
    private String publicationDate;


    public Composition(String title, String description, Set<Genre> compositionGenres) {
        this.description = description;
        this.title = title;
        this.compositionGenres = compositionGenres;
    }

    public Composition(Long id, @Size(max = 510) String description, @Size(max = 100) String title, User author, Set<Genre> compositionGenres) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.author = author;
        this.compositionGenres = compositionGenres;
    }

    public Composition() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<Genre> getCompositionGenres() {
        return compositionGenres;
    }

    public void setCompositionGenres(Set<Genre> compositionGenres) {
        this.compositionGenres = compositionGenres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {
        return "Composition{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", compositionGenres=" + compositionGenres +
                ", publicationDate='" + publicationDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Composition that = (Composition) object;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(title, that.title) &&
                Objects.equals(author, that.author) &&
                Objects.equals(compositionGenres, that.compositionGenres) &&
                Objects.equals(publicationDate, that.publicationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, title, author, compositionGenres, publicationDate);
    }
}
