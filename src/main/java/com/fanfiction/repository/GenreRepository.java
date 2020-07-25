package com.fanfiction.repository;

import com.fanfiction.models.Composition;
import com.fanfiction.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Override
    List<Genre> findAll();
}
