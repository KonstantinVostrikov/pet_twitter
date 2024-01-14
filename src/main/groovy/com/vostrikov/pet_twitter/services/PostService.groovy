package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.Post

interface PostService {

    Post createPost(Post post)
    Post editPost(Post post)
    void deletePost(String postId)

}