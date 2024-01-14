package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.User

interface UserService {
    User createUser(User user)

    User findById(String id)

    void deleteById(String id)

    User edit(User user)
}