package com.fanfiction.DTO;

import java.util.Objects;

public class GenresNewCompositionDTO {
    private Long id;
private String genreName;

    public GenresNewCompositionDTO(Long id, String genreName) {
        this.id = id;
        this.genreName = genreName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GenresNewCompositionDTO that = (GenresNewCompositionDTO) object;
        return Objects.equals(id, that.id) &&
                Objects.equals(genreName, that.genreName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genreName);
    }
}
