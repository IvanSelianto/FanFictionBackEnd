package com.fanfiction.controllers;

import com.fanfiction.DTO.CompositionDTO;
import com.fanfiction.models.Composition;
import com.fanfiction.payload.request.CompositionRequest;
import com.fanfiction.services.CompositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class CompositionController {
    @Autowired
    private CompositionService compositionService;

    @PostMapping("/savecomposition")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Composition saveComposition(@RequestBody CompositionRequest compositionRequest, Authentication authentication) {
        return compositionService.saveComposition(compositionRequest, authentication);
    }

    @GetMapping("/getcomposition/{compositionId}")
    public Composition getComposition(@PathVariable Long compositionId) {
        return compositionService.findCompositionById(compositionId);
    }

    @GetMapping("/getcompositionsforcurrentuser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<CompositionDTO> getCompositionsForCurrentUser(Authentication authentication) {
        return compositionService.getCompositionsForCurrentUser(authentication);
    }

    @DeleteMapping("/deletecomposition/{compositionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteComposition(@PathVariable Long compositionId) {
        compositionService.deleteComposition(compositionId);
    }

    @GetMapping("/allcompositions")
    public List<Composition> allCompositions() {
        return compositionService.getAllCompositions().stream()
                .sorted((composition1, composition2) -> {
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(composition2.getPublicationDate())
                                .compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(composition1.getPublicationDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }).collect(Collectors.toList());
    }

}
