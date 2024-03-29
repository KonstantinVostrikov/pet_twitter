package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.Like
import com.vostrikov.pet_twitter.entity.Post
import com.vostrikov.pet_twitter.exceptions.post.MissingPostIdException
import com.vostrikov.pet_twitter.exceptions.post.PostAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.post.PostNotExistException
import com.vostrikov.pet_twitter.exceptions.post.PostSameException
import com.vostrikov.pet_twitter.repository.PostRepository
import com.vostrikov.pet_twitter.services.CommentService
import com.vostrikov.pet_twitter.services.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime

@Service
@Transactional
class PostServiceImpl implements PostService {

    PostRepository postRepository
    CommentService commentService

    @Autowired
    PostServiceImpl(PostRepository postRepository, CommentService commentService) {
        this.postRepository = postRepository
        this.commentService = commentService
    }

    @Override
    Post createPost(Post post) {

        if (post.id != null) throw new PostAlreadyExistException()
        if (post.content == null || post.content.isEmpty()) throw new RuntimeException("Post of comment can't be empty")
        if (post.likes == null) {
            post.likes = new HashMap<>()
        }

        if (!post.likes.isEmpty()) throw new RuntimeException("New post can't have likes")

        post.setId(UUID.randomUUID().toString())
        def now = LocalDateTime.now()
        post.setCreatedAt(now)
        post.setModifiedAt(now)

        def createdPost = postRepository.save(post)
        return createdPost
    }

    @Override
    Post findPost(String postId) {
        if (postId == null || postId.empty) throw new MissingPostIdException()
        def optionalPost = postRepository.findById(postId)
        if (optionalPost.empty) throw new PostNotExistException()

        def post = optionalPost.get()
        def comments = commentService.fetchComments(postId)
        post.comments = comments
        return post
    }

    @Override
    List<Post> findPostsByIds(Iterable<String> postIds) {
        return enrichWithComments(postRepository.findAllById(postIds))
    }

    @Override
    List<Post> findPostsByUserIds(Iterable<String> userIds) {
        enrichWithComments(postRepository.findByUserIdInOrderByCreatedAtDesc(userIds))
    }

    @Override
    List<Post> findParticularUserPosts(String userId) {
        enrichWithComments(postRepository.findAllByUserId(userId))
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
        if (old.userId != post.userId) {
            throw new RuntimeException("Post userId can't be changed")
        }

        // check post the same
        if (old.equals(post)) {
            throw new PostSameException()
        }

        def now = LocalDateTime.now()
        post.setCreatedAt(old.createdAt)
        post.setModifiedAt(now)

        // do not write comments in post entity
        post.comments = null
        def edited = postRepository.save(post)
        return enrichWithComments([edited]).first()
    }

    @Override
    void deletePost(String postId) {
        postRepository.findById(postId).orElseThrow { new PostNotExistException() }
        postRepository.deleteById(postId)
    }

    @Override
    void addOrDeleteLike(Like like) {
        def optionalPost = postRepository.findById(like.postId)
        if (optionalPost.empty) {
            throw new RuntimeException("Can not find a post")
        }

        def post = optionalPost.get()
        def likeMap = post.likes
        def hasLike = likeMap.get(like.userId)

        if (hasLike) {
            likeMap.remove(like.userId)
        } else {
            likeMap.put(like.userId, true)
        }

        postRepository.save(post)
    }

    private List<Post> enrichWithComments(List<Post> posts) {
        posts.each { it ->
            it.comments = commentService.fetchComments(it.id)
        }
    }

}
