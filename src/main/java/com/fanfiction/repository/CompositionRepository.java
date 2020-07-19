package com.fanfiction.repository;

import com.fanfiction.models.Composition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompositionRepository extends JpaRepository<Composition, Long> {
    List<Composition> getCompositionsByAuthorId(Long authorId);
}
