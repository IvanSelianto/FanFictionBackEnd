package com.fanfiction.services;

import com.fanfiction.DTO.CompositionDTO;
import com.fanfiction.DTO.CompositionHomeDTO;
import com.fanfiction.DTO.CompositionProfileDTO;
import com.fanfiction.DTO.GenresNewCompositionDTO;
import com.fanfiction.models.Composition;
import com.fanfiction.models.Genre;
import com.fanfiction.repository.ChapterRepository;
import com.fanfiction.repository.CompositionRepository;
import com.fanfiction.repository.GenreRepository;
import com.fanfiction.repository.UserRepository;
import com.fanfiction.security.securityservices.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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


    public List<GenresNewCompositionDTO> getAllGenres() {
        return genreRepository.findAll().stream().map(genre -> new GenresNewCompositionDTO(genre.getId(), genre.getGenreName())).collect(Collectors.toList());
    }


    public Long saveComposition(CompositionDTO compositionDTO, Authentication authentication) {

        Composition composition = new Composition(
                compositionDTO.getTitle(),
                compositionDTO.getDescription(),
                compositionDTO.getCompositionGenres().stream()
                        .map(genresNewCompositionDTO ->
                                new Genre(genresNewCompositionDTO.getId()
                                        , genresNewCompositionDTO.getGenreName()))
                        .collect(Collectors.toSet()));
        composition.setAuthor(userRepository.findByUsername(((UserDetailsImpl) authentication.getPrincipal()).getUsername()).get());
        composition.setPublicationDate(String.valueOf(new java.sql.Timestamp(new Date().getTime())).replaceAll("\\.\\d+", ""));
        if (compositionDTO.getCompositionId() != null) {
            composition.setId(compositionDTO.getCompositionId());
        }
        compositionRepository.save(composition);

        return composition.getId();


    }


    public CompositionDTO findCompositionById(Long compositionId) {
        Composition composition = compositionRepository.findById(compositionId).get();
        return new CompositionDTO(
                composition.getAuthor(),
                composition.getTitle(),
                composition.getDescription(),
                composition.getCompositionGenres().stream()
                        .map(genre -> new GenresNewCompositionDTO(genre.getId(), genre.getGenreName()))
                        .collect(Collectors.toSet()),
                compositionId);
    }

    public List<CompositionProfileDTO> getCompositionsForCurrentUser(Authentication authentication) {
        return compositionRepository
                .getCompositionsByAuthorId(userRepository.findByUsername(authentication.getName()).get().getId())
                .stream().map(composition ->
                        new CompositionProfileDTO(composition.getId(),
                                composition.getTitle(),
                                composition.getDescription(),
                                chapterRepository.findAllByComposition(composition).size()))
                .collect(Collectors.toList());
    }

    public void deleteComposition(Long compositionId) {
        compositionRepository.deleteById(compositionId);
    }

    public List<CompositionHomeDTO> getAllCompositions() {
        return compositionRepository.findAll().stream().map(composition -> new CompositionHomeDTO(
                composition.getId(),
                composition.getTitle(),
                composition.getDescription(),
                composition.getCompositionGenres(),
                composition.getPublicationDate()))
                .sorted((compositionHomeDTO1, compositionHomeDTO2) -> {
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(compositionHomeDTO2.getPublicationDate())
                                .compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(compositionHomeDTO1.getPublicationDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }).collect(Collectors.toList());

    }

}
