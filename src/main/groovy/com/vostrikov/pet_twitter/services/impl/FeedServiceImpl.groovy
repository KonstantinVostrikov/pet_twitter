package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.Feed
import com.vostrikov.pet_twitter.dto.Post
import com.vostrikov.pet_twitter.services.FeedService
import com.vostrikov.pet_twitter.services.PostService
import com.vostrikov.pet_twitter.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeedServiceImpl implements FeedService {

    PostService postService
    UserService userService

    @Autowired
    FeedServiceImpl(PostService postService, UserService userService) {
        this.postService = postService
        this.userService = userService
    }


    // TODO: make pagination
    @Override
    List<Post> receive(Feed feed) {

        // check dto
        if (feed.from == null || feed.from.empty) throw new RuntimeException("Feed dto is wrong")
        if (feed.to == null || feed.to.empty) throw new RuntimeException("Feed dto is wrong")

        // user ask his subscriptions
        boolean ownFeed = feed.from.equals(feed.to)

        if (ownFeed) {
            // check user exist
            def user = userService.findById(feed.from)
            HashSet set = new HashSet(user.subscriptions)
            set.add(user.id)
            return postService.findPostsByUserIds(set)
        } else {

            // Check users exist
            userService.findById(feed.from)
            def toUser = userService.findById(feed.to)
            return postService.findParticularUserPosts(toUser.id)
        }

    }

    @Override
    List<Post> own(String userId) {
        // TODO: make pagination
        def user = userService.findById(userId)
        postService.findParticularUserPosts(user.id)
    }

    @Override
    List<Post> favoritesPosts(String userId) {
        def user = userService.findById(userId)
        if (!user.favoritePosts.empty) {
            return postService.findPostsByIds(user.favoritePosts)
        } else {
            return []
        }
    }
}
