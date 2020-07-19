package com.fanfiction.services;

import com.fanfiction.DTO.CompositionDTO;
import com.fanfiction.models.Composition;
import com.fanfiction.models.Genre;
import com.fanfiction.payload.request.CompositionRequest;
import com.fanfiction.repository.ChapterRepository;
import com.fanfiction.repository.CompositionRepository;
import com.fanfiction.repository.GenreRepository;
import com.fanfiction.repository.UserRepository;
import com.fanfiction.security.securityservices.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompositionService {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CompositionRepository compositionRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompositionService compositionService;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }


    public Composition saveComposition(CompositionRequest compositionRequest, Authentication authentication) {
        Set<Genre> compositionGenres = compositionService.getAllGenres()
                .stream().filter(genre -> compositionRequest.getCompositionGenres().contains(genre.getGenreName())).collect(Collectors.toSet());

        Composition composition = new Composition(compositionRequest.getTitle(), compositionRequest.getDescription(), compositionGenres);
        composition.setAuthor(userRepository.findByUsername(((UserDetailsImpl) authentication.getPrincipal()).getUsername()).get());
        composition.setPublicationDate(String.valueOf(new java.sql.Timestamp(new Date().getTime())).replaceAll("\\.\\d+", ""));
        if (compositionRequest.getCompositionId() != null) {
            composition.setId(compositionRequest.getCompositionId());
        }
        return compositionRepository.save(composition);
    }


    public Composition findCompositionById(Long compositionId) {
        return compositionRepository.findById(compositionId).get();
    }

    public List<CompositionDTO> getCompositionsForCurrentUser(Authentication authentication) {
        return compositionRepository
                .getCompositionsByAuthorId(userRepository.findByUsername(authentication.getName()).get().getId())
                .stream().map(composition ->
                        new CompositionDTO(composition.getId(),
                                composition.getTitle(),
                                composition.getDescription(),
                                chapterRepository.findAllByComposition(composition).size()))
                .collect(Collectors.toList());
    }

    public void deleteComposition(Long compositionId) {
        compositionRepository.deleteById(compositionId);
    }

    public List<Composition> getAllCompositions() {
        return compositionRepository.findAll();
    }

}
