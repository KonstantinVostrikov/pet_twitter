package com.vostrikov.pet_twitter.dto

class User {
    String id
    String nickName
    String firstName
    String secondName
    String email
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
