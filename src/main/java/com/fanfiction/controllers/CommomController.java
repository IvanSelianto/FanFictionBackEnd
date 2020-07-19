package com.fanfiction.controllers;

import com.fanfiction.models.Composition;
import com.fanfiction.models.Genre;
import com.fanfiction.payload.request.EditNameRequest;
import com.fanfiction.services.CompositionService;
import com.fanfiction.services.SearchService;
import com.fanfiction.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class CommomController {
    @Autowired
    private CompositionService compositionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SearchService searchService;


    @GetMapping("/allGenres")
    public List<String> getGenres() {
        return compositionService.getAllGenres().stream().map(Genre::getGenreName).collect(Collectors.toList());
    }

    @PostMapping("/editusername")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> editUsername(@RequestBody EditNameRequest editNameRequest) {
        return userService.editUsername(editNameRequest);
    }

    @GetMapping("/search/{searchRequest}")
    public Set<Composition> search(@PathVariable String searchRequest) throws InterruptedException {
        return searchService.initializeHibernateSearch(searchRequest);
    }
}


