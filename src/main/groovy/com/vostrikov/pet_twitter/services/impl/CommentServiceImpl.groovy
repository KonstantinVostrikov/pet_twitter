package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.Comment
import com.vostrikov.pet_twitter.exceptions.comment.CommentAlreadyExistException
import com.vostrikov.pet_twitter.repository.CommentRepository
import com.vostrikov.pet_twitter.services.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository

    @Autowired
    CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository
    }

    @Override
    Comment addComment(Comment comment) {

        // check before create
        if (comment.id != null) throw new CommentAlreadyExistException()
        if (comment.content == null || comment.content.isEmpty()) throw new RuntimeException("Content of comment can't be empty")
        if (comment.postId == null || comment.postId.isEmpty()) throw new RuntimeException("Comment postId field can't be empty")
        if (comment.userId == null || comment.userId.isEmpty()) throw new RuntimeException("Author of comment can't be empty")

        comment.id = UUID.randomUUID().toString()
        comment.createdAt = LocalDateTime.now()

        return commentRepository.save(comment)
    }

    @Override
    List<Comment> fetchComments(String postId){
        if (postId == null || postId.isEmpty()) throw new RuntimeException("Comment postId field can't be empty")
        // TODO: make pagination
        return commentRepository.findByPostId(postId)
    }
}
