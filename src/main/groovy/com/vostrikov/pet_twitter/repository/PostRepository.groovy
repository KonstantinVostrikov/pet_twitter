package com.vostrikov.pet_twitter.repository

import com.vostrikov.pet_twitter.dto.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findAllByUserId(String userId)

    List<Post> findByUserIdInOrderByCreatedAtDesc(Iterable<String> userIds)

}
