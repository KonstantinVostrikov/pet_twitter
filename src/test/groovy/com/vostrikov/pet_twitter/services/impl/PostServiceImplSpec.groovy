package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.Comment
import com.vostrikov.pet_twitter.dto.Like
import com.vostrikov.pet_twitter.dto.Post
import com.vostrikov.pet_twitter.exceptions.post.MissingPostIdException
import com.vostrikov.pet_twitter.exceptions.post.PostAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.post.PostNotExistException
import com.vostrikov.pet_twitter.repository.PostRepository
import com.vostrikov.pet_twitter.services.CommentService
import com.vostrikov.pet_twitter.services.PostService
import spock.lang.Specification

class PostServiceImplSpec extends Specification {

    def postRepository = Mock(PostRepository.class)
    def commentService = Mock(CommentService.class)
    PostService postService = new PostServiceImpl(postRepository, commentService)

    // create
    def "createPost should create and return a new post"() {
        given:
        def post = new Post(content: "Some content")

        when:
        def result = postService.createPost(post)

        then:
        1 * postRepository.save(post) >> post

        result != null
        result.content == post.content
        result.likes == [:]
        result.createdAt != null
        result.modifiedAt != null
    }

    def "createPost should throw PostAlreadyExistException if post id is not null"() {
        given:
        def post = new Post(id: "someId", content: "Some content")

        when:
        postService.createPost(post)

        then:
        thrown(PostAlreadyExistException)
    }

    def "createPost should throw RuntimeException if post content is empty"() {
        given:
        def post = new Post(content: "")

        when:
        postService.createPost(post)

        then:
        def e = thrown(RuntimeException)
        e.message == "Post of comment can't be empty"

    }

    def "createPost should throw RuntimeException if post likes is not empty"() {
        given:
        def post = new Post(content: "content", likes: [user1: true, user2: false])

        when:
        postService.createPost(post)

        then:
        def e = thrown(RuntimeException)
        e.message == "New post can't have likes"
    }


    // find posts
    def "findPost should return a post with comments for a valid post ID"() {
        given:
        def postId = "somePostId"
        def post = new Post(id: postId, content: "Some content")
        def comments = [new Comment(id: 1, postId: postId, content: "Comment 1"),
                        new Comment(id: 2, postId: postId, content: "Comment 2")]

        postRepository.findById(postId) >> Optional.of(post)
        commentService.fetchComments(postId) >> comments

        when:
        def result = postService.findPost(postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(post)
        1 * commentService.fetchComments(postId) >> comments

        result == post
        result.comments == comments
    }

    def "findPost should throw MissingPostIdException for a null post ID"() {
        given:
        def postId = null

        when:
        postService.findPost(postId)

        then:
        thrown(MissingPostIdException)
    }

    def "findPost should throw MissingPostIdException for an empty post ID"() {
        given:
        def postId = ""

        when:
        postService.findPost(postId)

        then:
        thrown(MissingPostIdException)
    }

    // findPostsByIds
    def "findPostsByIds should return a list of posts enriched with comments"() {
        given:
        def postIds = ["postId1", "postId2"]
        def posts = [new Post(id: "postId1", content: "Content 1"),
                     new Post(id: "postId2", content: "Content 2")]

        postRepository.findAllById(postIds) >> posts
        commentService.fetchComments("postId1") >> [new Comment(id: 1, postId: "postId1", content: "Comment 1")]
        commentService.fetchComments("postId2") >> [new Comment(id: 2, postId: "postId2", content: "Comment 2")]

        when:
        def result = postService.findPostsByIds(postIds)

        then:
        1 * postRepository.findAllById(postIds) >> posts
        1 * commentService.fetchComments("postId1") >> [new Comment(id: 1, postId: "postId1", content: "Comment 1")]
        1 * commentService.fetchComments("postId2") >> [new Comment(id: 2, postId: "postId2", content: "Comment 2")]

        result.size() == 2
        result[0].id == "postId1"
        result[0].content == "Content 1"
        result[0].comments.size() == 1
        result[1].id == "postId2"
        result[1].content == "Content 2"
        result[1].comments.size() == 1
    }

    // findParticularUserPosts
    def "findParticularUserPosts should return a list of posts for a particular user enriched with comments"() {
        given:
        def userId = "someUserId"
        def userPosts = [new Post(id: "postId1", userId: userId, content: "Content 1"),
                         new Post(id: "postId2", userId: userId, content: "Content 2")]

        postRepository.findAllByUserId(userId) >> userPosts
        commentService.fetchComments("postId1") >> [new Comment(id: 1, postId: "postId1", content: "Comment 1")]
        commentService.fetchComments("postId2") >> [new Comment(id: 2, postId: "postId2", content: "Comment 2")]

        when:
        def result = postService.findParticularUserPosts(userId)

        then:
        1 * postRepository.findAllByUserId(userId) >> userPosts
        1 * commentService.fetchComments("postId1") >> [new Comment(id: 1, postId: "postId1", content: "Comment 1")]
        1 * commentService.fetchComments("postId2") >> [new Comment(id: 2, postId: "postId2", content: "Comment 2")]

        result.size() == 2
        result[0].id == "postId1"
        result[0].userId == userId
        result[0].content == "Content 1"
        result[0].comments.size() == 1
        result[1].id == "postId2"
        result[1].userId == userId
        result[1].content == "Content 2"
        result[1].comments.size() == 1
    }

    // edit
    def "editPost should edit and return the post with comments"() {
        given:
        def postId = "somePostId"
        def userId = "someUserId"
        List comments = [new Comment(id: 1, postId: postId, content: "Comment 1")]
        def oldPost = new Post(id: postId, userId: userId, content: "Old Content", comments: comments)
        def newPost = new Post(id: postId, userId: userId, content: "New Content", comments: comments)
        def newPostWithoutComments = new Post(id: postId, userId: userId, content: "New Content", comments: null)
        postRepository.findById(postId) >> Optional.of(oldPost)
        commentService.fetchComments(postId) >> comments

        when:
        def result = postService.editPost(newPost)

        then:
        1 * postRepository.findById(postId) >> Optional.of(oldPost)
        1 * commentService.fetchComments(postId) >> comments
        1 * postRepository.save(newPostWithoutComments) >> newPost

        result.id == postId
        result.userId == userId
        result.content == "New Content"
        result.comments.size() == 1
    }


    def "deletePost should delete the post with the given postId"() {
        given:
        def postId = "somePostId"
        def existingPost = new Post(id: postId, content: "Some Content")

        postRepository.findById(postId) >> Optional.of(existingPost)

        when:
        postService.deletePost(postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * postRepository.deleteById(postId)
    }

    def "deletePost should throw PostNotExistException if the post does not exist"() {
        given:
        def postId = "nonExistentPostId"

        postRepository.findById(postId) >> Optional.empty()

        when:
        postService.deletePost(postId)

        then:
        1 * postRepository.findById(postId) >> Optional.empty()
        0 * postRepository.deleteById(_)
        thrown(PostNotExistException)
    }

    // likes
    def "addOrDeleteLike should add a like for a post"() {
        given:
        def postId = "somePostId"
        def userId = "someUserId"
        def existingPost = new Post(id: postId, likes: new HashMap<String, Boolean>())

        postRepository.findById(postId) >> Optional.of(existingPost)

        when:
        postService.addOrDeleteLike(new Like(postId: postId, userId: userId))

        then:
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * postRepository.save(_ as Post)

        existingPost.likes.size() == 1
    }

    def "addOrDeleteLike should delete a like for a post"() {
        given:
        def postId = "somePostId"
        def userId = "someUserId"
        def existingPost = new Post(id: postId, likes: [someUserId: true])

        postRepository.findById(postId) >> Optional.of(existingPost)

        when:
        postService.addOrDeleteLike(new Like(postId: postId, userId: userId))

        then:
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * postRepository.save(_ as Post)

        existingPost.likes.size() == 0
    }

    def "addOrDeleteLike should throw RuntimeException if the post does not exist"() {
        given:
        def postId = "nonExistentPostId"

        postRepository.findById(postId) >> Optional.empty()

        when:
        postService.addOrDeleteLike(new Like(postId: postId, userId: "someUserId"))

        then:
        1 * postRepository.findById(postId) >> Optional.empty()
        0 * postRepository.save(_ as Post)
        def e = thrown(RuntimeException)
        e.message == "Can not find a post"
    }


}
