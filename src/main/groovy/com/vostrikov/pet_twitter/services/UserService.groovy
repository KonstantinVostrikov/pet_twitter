package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.FavoritePost
import com.vostrikov.pet_twitter.dto.Subscription
import com.vostrikov.pet_twitter.dto.UserDto
import com.vostrikov.pet_twitter.entity.User

interface UserService {
    UserDto createUser(User user)

    UserDto findById(String id)

    void deleteById(String id)

    UserDto edit(User user)

    Boolean subscribe(Subscription subscription)

    Boolean unsubscribe(Subscription subscription)

    void favoritePost(FavoritePost favoritePost)

    User findByEmail(String email)
}