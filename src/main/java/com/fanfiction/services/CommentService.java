package com.fanfiction.services;

import com.fanfiction.DTO.CommentDTO;
import com.fanfiction.models.Comment;
import com.fanfiction.models.Composition;
import com.fanfiction.models.Genre;
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
                        comment.getCommentAuthor(), comment.getText())).sorted((comment1, comment2)
                        -> Integer.parseInt(String.valueOf(comment2.getId() - comment1.getId()))).collect(Collectors.toList());

    }

    public CommentDTO addComment(CommentDTO commentDTO, Authentication authentication) {
        System.out.println(commentDTO);
        Comment comment = new Comment();
        comment.setCommentAuthor(userRepository.findByUsername(authentication.getName()).get());
        comment.setComposition(new Composition(
                commentDTO.getCompositionDTO().getCompositionId(),
                commentDTO.getCompositionDTO().getDescription(),
                commentDTO.getCompositionDTO().getTitle(),
                commentDTO.getCompositionDTO().getAuthor(),
                commentDTO.getCompositionDTO().getCompositionGenres().stream()
                        .map(genre -> new Genre(genre.getId(), genre.getGenreName())).collect(Collectors.toSet())
        ));
        comment.setText(commentDTO.getText());
        commentRepository.save(comment);
        return commentDTO;
    }
}
