package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.Feed
import com.vostrikov.pet_twitter.dto.Post
import com.vostrikov.pet_twitter.dto.User
import com.vostrikov.pet_twitter.services.FeedService
import com.vostrikov.pet_twitter.services.PostService
import com.vostrikov.pet_twitter.services.UserService
import spock.lang.Specification

import java.time.LocalDateTime

class FeedServiceImplSpec extends Specification {

    def postService = Mock(PostService.class)
    def userService = Mock(UserService.class)
    FeedService feedService = new FeedServiceImpl(postService, userService)

    // receive
    def "receive should return posts for the user's subscriptions if feed is own"() {
        given:
        def userId = "userId"
        def ownFeed = new Feed(from: userId, to: userId)
        def user = new User(id: userId, email: "test@example.com", nickName: "testUser", subscriptions: ["subscription1", "subscription2"])
        def posts = [
                new Post(id: "post1", content: "Content 1", userId: "subscription1", createdAt: LocalDateTime.now()),
                new Post(id: "post2", content: "Content 2", userId: "subscription2", createdAt: LocalDateTime.now())
        ]
        userService.findById(userId) >> user
        HashSet sub = ["userId", "subscription1", "subscription2"]
        postService.findPostsByUserIds(sub) >> posts

        when:
        def result = feedService.receive(ownFeed)

        then:
        1 * userService.findById(userId) >> user
        1 * postService.findPostsByUserIds(sub) >> posts

        result == posts
    }

    def "receive should return posts for a particular user if feed is not own"() {
        given:
        def fromUserId = "fromUserId"
        def toUserId = "toUserId"
        def feed = new Feed(from: fromUserId, to: toUserId)
        def fromUser = new User(id: fromUserId, email: "from@example.com", nickName: "fromUser")
        def toUser = new User(id: toUserId, email: "to@example.com", nickName: "toUser")
        def posts = [
                new Post(id: "post1", content: "Content 1", userId: toUserId, createdAt: LocalDateTime.now()),
                new Post(id: "post2", content: "Content 2", userId: toUserId, createdAt: LocalDateTime.now())
        ]

        userService.findById(fromUserId) >> fromUser
        userService.findById(toUserId) >> toUser
        postService.findParticularUserPosts(toUserId) >> posts

        when:
        def result = feedService.receive(feed)

        then:
        1 * userService.findById(fromUserId) >> fromUser
        1 * userService.findById(toUserId) >> toUser
        1 * postService.findParticularUserPosts(toUserId) >> posts

        result == posts
    }

    def "receive should throw RuntimeException if from field is empty"() {
        when:
        feedService.receive(new Feed(from: "", to: "toUserId"))

        then:
        def e = thrown(RuntimeException)
        e.message == "Feed dto is wrong"
    }

    def "receive should throw RuntimeException if to field is empty"() {
        when:
        feedService.receive(new Feed(from: "fromUserId", to: ""))

        then:
        def e = thrown(RuntimeException)
        e.message == "Feed dto is wrong"
    }

    // own
    def "own should return posts for a valid userId"() {
        given:
        def userId = "userId"
        def posts = [
                new Post(id: "post1", content: "Content 1", userId: userId, createdAt: LocalDateTime.now()),
                new Post(id: "post2", content: "Content 2", userId: userId, createdAt: LocalDateTime.now())
        ]
        postService.findOwnPosts(userId) >> posts
        userService.findById(userId) >> new User(id: userId)

        when:
        def result = feedService.own(userId)

        then:
        1 * postService.findOwnPosts(userId) >> posts
        result == posts
    }

    def "own should throw RuntimeException if userId is empty"() {
        given:
        def userId = ""
        userService.findById(userId) >> { throw new RuntimeException("User ID can't be empty") }

        when:
        feedService.own(userId)

        then:
        def e = thrown(RuntimeException)
        e.message == "User ID can't be empty"
    }

    // favoritePosts
    def "favoritesPosts should return a list of favorite posts for a given user"() {
        given:
        def userId = "someUserId"
        Set fav = new HashSet([1, 2, 3])
        def user = new User(id: userId, email: "from@example.com", nickName: "fromUser", favoritePosts: fav)
        def expectedPosts = [new Post(id: 1), new Post(id: 2), new Post(id: 3)]

        userService.findById(_) >> user
        postService.findPostsByIds(fav) >> expectedPosts


        when:
        def result = feedService.favoritesPosts(userId)

        then:
        1 * userService.findById(userId) >> user
        1 * postService.findPostsByIds(fav) >> expectedPosts

        result == expectedPosts
    }

    def "favoritesPosts should return an empty list if user has no favorite posts"() {
        given:
        def userId = "someUserId"
        def user = new User(id: userId, favoritePosts: [])
        userService.findById(userId) >> user

        when:
        def result = feedService.favoritesPosts(userId)

        then:
        1 * userService.findById(userId) >> user
        0 * postService.findPostsByIds(_)

        result == []
    }

}
