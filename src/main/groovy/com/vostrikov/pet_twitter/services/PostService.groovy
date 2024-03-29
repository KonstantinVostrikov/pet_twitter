package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.Like
import com.vostrikov.pet_twitter.entity.Post

interface PostService {

    Post createPost(Post post)

    Post findPost(String postId)

    List<Post> findPostsByIds(Iterable<String> postIds)

    List<Post> findPostsByUserIds(Iterable<String> userIds)

    List<Post> findParticularUserPosts(String userId)

    Post editPost(Post post)

    void deletePost(String postId)

    void addOrDeleteLike(Like like)

}