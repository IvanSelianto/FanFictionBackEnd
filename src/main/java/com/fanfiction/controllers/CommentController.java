package com.fanfiction.controllers;

import com.fanfiction.DTO.CommentDTO;
import com.fanfiction.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fanfic")
public class CommentController {

    private final SimpMessagingTemplate template;

    @Autowired
    CommentController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Autowired
    private CommentService commentService;

    @GetMapping("/getcommentsbycompositionid/{compositionId}")
    public List<CommentDTO> getCommentsByCompositionId(@PathVariable Long compositionId) {
        return commentService.getCommentsByCompositionId(compositionId);
    }


    @PostMapping("/addcomment")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void addComment(@RequestBody CommentDTO commentDTO, Authentication authentication) {
        System.out.println(commentDTO.toString());
        this.template.convertAndSend("/message", commentService.addComment(commentDTO, authentication));
    }
}
