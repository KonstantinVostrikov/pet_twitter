package com.vostrikov.pet_twitter.dto

import com.vostrikov.pet_twitter.entity.User

class UserDto {
    String id
    String nickName
    String firstName
    String secondName
    String email
    String role
    String phoneNumber
    Set<String> subscriptions
    Set<String> favoritePosts

    UserDto(User user) {
        this.id = user.id
        this.nickName = user.nickName
        this.firstName = user.firstName
        this.secondName = user.secondName
        this.email = user.email
        this.role = user.role
        this.phoneNumber = user.phoneNumber
        this.subscriptions = user.subscriptions
        this.favoritePosts = user.favoritePosts
    }
}
