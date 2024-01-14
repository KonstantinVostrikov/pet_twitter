package com.vostrikov.pet_twitter.repository

import com.vostrikov.pet_twitter.dto.Comment
import com.vostrikov.pet_twitter.dto.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId)

}
