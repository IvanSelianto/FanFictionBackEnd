package com.fanfiction.controllers;

import com.fanfiction.DTO.GenresNewCompositionDTO;
import com.fanfiction.models.Composition;
import com.fanfiction.DTO.UserDTO;
import com.fanfiction.services.CompositionService;
import com.fanfiction.services.SearchService;
import com.fanfiction.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/fanfic")
public class CommomController {
    @Autowired
    private CompositionService compositionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SearchService searchService;


    @GetMapping("/allGenres")
    public List<GenresNewCompositionDTO> getGenres() {
        return compositionService.getAllGenres();
    }

    @PostMapping("/editusername")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> editUsername(@RequestBody UserDTO userDTO) {
        return userService.editUsername(userDTO);
    }

    @GetMapping("/search/{searchRequest}")
    public Set<Composition> search(@PathVariable String searchRequest) throws InterruptedException {
        return searchService.initializeHibernateSearch(searchRequest);
    }
}


