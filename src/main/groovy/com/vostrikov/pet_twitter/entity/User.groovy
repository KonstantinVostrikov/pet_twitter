package com.vostrikov.pet_twitter.entity

import com.fasterxml.jackson.annotation.JsonIgnore

class User {
    String id
    String nickName
    String firstName
    String secondName
    String email
    @JsonIgnore
    String password
    String role
    String phoneNumber
    Set<String> subscriptions
    Set<String> favoritePosts

    @Override
    String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                '}'
    }
}
