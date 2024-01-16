package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.entity.Comment
import com.vostrikov.pet_twitter.exceptions.comment.CommentAlreadyExistException
import com.vostrikov.pet_twitter.repository.CommentRepository
import com.vostrikov.pet_twitter.services.CommentService
import spock.lang.Specification

import java.time.LocalDateTime

class CommentServiceImplSpec extends Specification {

    def commentRepository = Mock(CommentRepository.class)
    CommentService commentService = new CommentServiceImpl(commentRepository)

    // add
    def "addComment should create a new comment if all fields are valid"() {
        given:
        def commentWithoutId = new Comment(content: "Test content", postId: "postId", userId: "userId")
        def savedComment = new Comment(id: "commentId", content: "Test content", postId: "postId", userId: "userId", createdAt: LocalDateTime.now())

        commentRepository.save(_) >> savedComment

        when:
        def result = commentService.addComment(commentWithoutId)

        then:
        1 * commentRepository.save(commentWithoutId) >> savedComment

        result == savedComment
        result.id == "commentId"
        result.createdAt != null
    }

    def "addComment should throw CommentAlreadyExistException if comment has an ID"() {
        when:
        commentService.addComment(new Comment(id: "existingId", content: "Test content", postId: "postId", userId: "userId"))

        then:
        thrown(CommentAlreadyExistException)
    }

    def "addComment should throw RuntimeException if content is empty"() {
        when:
        commentService.addComment(new Comment(content: "", postId: "postId", userId: "userId"))

        then:
        def e = thrown(RuntimeException)
        e.message == "Content of comment can't be empty"
    }


    def "addComment should throw RuntimeException if postId is empty"() {
        when:
        commentService.addComment(new Comment(content: "Test content", postId: "", userId: "userId"))

        then:
        def e = thrown(RuntimeException)
        e.message == "Comment postId field can't be empty"
    }

    def "addComment should throw RuntimeException if userId is empty"() {
        when:
        commentService.addComment(new Comment(content: "Test content", postId: "postId", userId: ""))

        then:
        def e = thrown(RuntimeException)
        e.message == "Author of comment can't be empty"
    }

    // fetch
    def "fetchComments should return comments for a valid postId"() {
        given:
        def postId = "postId"
        def comments = [
                new Comment(id: "comment1", content: "Content 1", postId: postId, userId: "user1", createdAt: LocalDateTime.now()),
                new Comment(id: "comment2", content: "Content 2", postId: postId, userId: "user2", createdAt: LocalDateTime.now()),
                new Comment(id: "comment3", content: "Content 3", postId: postId, userId: "user3", createdAt: LocalDateTime.now())
        ]

        commentRepository.findByPostIdOrderByCreatedAtAsc(postId) >> comments

        when:
        def result = commentService.fetchComments(postId)

        then:
        1 * commentRepository.findByPostIdOrderByCreatedAtAsc(postId) >> comments

        result == comments
    }

    def "fetchComments should throw RuntimeException if postId is empty"() {
        when:
        commentService.fetchComments("")

        then:
        def e = thrown(RuntimeException)
        e.message == "Comment postId field can't be empty"
    }

    def "fetchComments should throw RuntimeException if postId is null"() {
        when:
        commentService.fetchComments(null)

        then:
        def e =  thrown(RuntimeException)
        e.message == "Comment postId field can't be empty"
    }
}
