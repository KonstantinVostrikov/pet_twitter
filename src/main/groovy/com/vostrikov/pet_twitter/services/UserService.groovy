package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.FavoritePost
import com.vostrikov.pet_twitter.dto.Subscription
import com.vostrikov.pet_twitter.dto.User

interface UserService {
    User createUser(User user)

    User findById(String id)

    void deleteById(String id)

    User edit(User user)

    Boolean subscribe(Subscription subscription)

    Boolean unsubscribe(Subscription subscription)

    void favoritePost(FavoritePost favoritePost)
}