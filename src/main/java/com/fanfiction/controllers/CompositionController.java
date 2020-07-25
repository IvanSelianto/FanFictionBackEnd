package com.fanfiction.controllers;

import com.fanfiction.DTO.CompositionDTO;
import com.fanfiction.DTO.CompositionHomeDTO;
import com.fanfiction.DTO.CompositionProfileDTO;
import com.fanfiction.services.CompositionService;
import com.fanfiction.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/api/fanfic")
public class CompositionController {
    @Autowired
    private CompositionService compositionService;
    @Autowired
    private PdfService pdfService;

    @PostMapping("/savecomposition")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Long saveComposition(@RequestBody CompositionDTO compositionDTO, Authentication authentication) {
        return compositionService.saveComposition(compositionDTO, authentication);
    }

    @GetMapping("/getcomposition/{compositionId}")
    public CompositionDTO getComposition(@PathVariable Long compositionId) {
        return compositionService.findCompositionById(compositionId);
    }

    @GetMapping("/getcompositionsforcurrentuser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<CompositionProfileDTO> getCompositionsForCurrentUser(Authentication authentication) {
        return compositionService.getCompositionsForCurrentUser(authentication);
    }

    @DeleteMapping("/deletecomposition/{compositionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteComposition(@PathVariable Long compositionId) {
        compositionService.deleteComposition(compositionId);
    }

    @GetMapping("/allcompositions")
    public List<CompositionHomeDTO> allCompositions() {
        return compositionService.getAllCompositions();
    }

    @GetMapping("/exporttopdf/{compositionId}")
    public void exportToPdf(@PathVariable Long compositionId, HttpServletResponse response) throws Exception {
        pdfService.exportToPdf(compositionId, response);
    }

}
