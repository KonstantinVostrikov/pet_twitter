package com.vostrikov.pet_twitter.repository

import com.vostrikov.pet_twitter.dto.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email)

    Optional<User> findByNickName(String nickName)
}
