package com.fanfiction.repository;

import com.fanfiction.models.Comment;
import com.fanfiction.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCompositionId(Long compositionId);

    List<Comment> findAllByCommentAuthorId(Long commentAuthorId);
}
