package com.vostrikov.pet_twitter.controllers

import com.vostrikov.pet_twitter.dto.Comment
import com.vostrikov.pet_twitter.dto.ResponseResult
import com.vostrikov.pet_twitter.services.CommentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController()
@RequestMapping("/comment")
class CommentController {

    @Autowired
    CommentService commentService

    @PostMapping("/add")
    def addComment(@RequestBody Comment comment) {
        try {
            def newComment = commentService.addComment(comment)
            return ResponseEntity.ok().body(new ResponseResult("Comment successfully added", newComment))
        } catch (Exception ex) {
            log.error(ex.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }
}
