package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.Feed
import com.vostrikov.pet_twitter.dto.Post

interface FeedService {
    List<Post> receive(Feed feed)

    List<Post> own(String userId)

    List<Post> favoritesPosts(String userId)
}