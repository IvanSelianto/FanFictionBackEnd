package com.fanfiction.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
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
                "id=" + id + "}";
    }
}
