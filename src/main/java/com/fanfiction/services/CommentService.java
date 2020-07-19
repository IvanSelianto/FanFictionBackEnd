package com.fanfiction.services;

import com.fanfiction.DTO.CommentDTO;
import com.fanfiction.models.Comment;
import com.fanfiction.payload.request.CommentRequest;
import com.fanfiction.repository.CommentRepository;
import com.fanfiction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;


    public List<CommentDTO> getCommentsByCompositionId(Long compositionId) {
        return commentRepository.findAllByCompositionId(compositionId).stream()
                .map(comment -> new CommentDTO(comment.getId(),
                        comment.getCommentAuthor(), comment.getText(), comment.getComposition())).sorted((comment1, comment2)
                        -> Integer.parseInt(String.valueOf(comment2.getId() - comment1.getId()))).collect(Collectors.toList());

    }

    public Comment addComment(CommentRequest commentRequest, Authentication authentication) {
        Comment comment = new Comment();
        comment.setCommentAuthor(userRepository.findByUsername(authentication.getName()).get());
        comment.setComposition(commentRequest.getComposition());
        comment.setText(commentRequest.getText());
        return commentRepository.save(comment);
    }
}
