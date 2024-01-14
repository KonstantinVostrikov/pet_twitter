package com.vostrikov.pet_twitter.dto

import java.time.LocalDateTime

class Comment {
    String id
    String postId
    String userId
    String content
    LocalDateTime createdAt
}
