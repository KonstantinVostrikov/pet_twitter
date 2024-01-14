package com.vostrikov.pet_twitter.controllers

import com.vostrikov.pet_twitter.dto.Like
import com.vostrikov.pet_twitter.dto.Post
import com.vostrikov.pet_twitter.dto.ResponseResult
import com.vostrikov.pet_twitter.services.PostService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController()
@RequestMapping("/posts")
class PostController {

    @Autowired
    PostService postService

    @PostMapping("/create")
    def createPost(@RequestBody Post post) {
        try {
            def newPost = postService.createPost(post)
            return ResponseEntity.ok().body(new ResponseResult("Post successfully created", newPost))
        } catch (Exception ex) {
            log.error(ex.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }

    @GetMapping("/find/{id}")
    def findPost(@PathVariable("id") String postId) {
        try {
            def post = postService.findPost(postId)
            return ResponseEntity.ok().body(new ResponseResult("Post successfully found", post))
        } catch (Exception ex) {
            log.error(ex.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }

    @PostMapping("/edit")
    def editPost(@RequestBody Post post) {
        try {
            def editedPost = postService.editPost(post)
            return ResponseEntity.ok().body(new ResponseResult("Post successfully edited", editedPost))
        } catch (Exception ex) {
            log.error(ex.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }

    @DeleteMapping("/delete/{id}")
    def deletePost(@PathVariable("id") String postId) {
        try {
            postService.deletePost(postId)
            return ResponseEntity.ok().body(new ResponseResult("Post successfully deleted"))
        } catch (Exception ex) {
            log.error(ex.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }

    @PostMapping("/like")
    def addOrDeleteLike(@RequestBody Like like) {
        try {
            postService.addOrDeleteLike(like)
            return ResponseEntity.ok().body(new ResponseResult("Like successfully processed"))
        } catch (Exception ex) {
            log.error(ex.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }

}
