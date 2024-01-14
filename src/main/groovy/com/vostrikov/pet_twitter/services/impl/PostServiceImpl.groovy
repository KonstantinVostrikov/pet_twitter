package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.Post
import com.vostrikov.pet_twitter.exceptions.post.PostAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.post.PostNotExistException
import com.vostrikov.pet_twitter.exceptions.post.PostSameException
import com.vostrikov.pet_twitter.repository.PostRepository
import com.vostrikov.pet_twitter.services.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class PostServiceImpl implements PostService {

    PostRepository postRepository

    @Autowired
    PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository
    }

    @Override
    Post createPost(Post post) {

        if (post.id != null) throw new PostAlreadyExistException()

        post.setId(UUID.randomUUID().toString())
        def now = LocalDateTime.now()
        post.setCreatedAt(now)
        post.setModifiedAt(now)

        def createdPost = postRepository.save(post)
        return createdPost
    }

    @Override
    Post editPost(Post post) {
        // check post exist
        def optionalOldPost = postRepository.findById(post.id)
        if (optionalOldPost.empty) {
            throw new PostNotExistException()
        }

        //check userId the same - userId can't be changed
        def old = optionalOldPost.get()
        if (old.userId != post.userId){
            throw new RuntimeException("Post userId can't be changed")
        }

        // check post the same
        if (old.equals(post)){
            throw new PostSameException()
        }

        def now = LocalDateTime.now()
        post.setCreatedAt(old.createdAt)
        post.setModifiedAt(now)
        def edited = postRepository.save(post)
        return edited
    }

    @Override
    void deletePost(String postId) {
        postRepository.findById(postId).orElseThrow { new PostNotExistException() }
        postRepository.deleteById(postId)
    }



}
