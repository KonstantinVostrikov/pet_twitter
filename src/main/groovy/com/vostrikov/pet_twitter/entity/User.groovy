package com.vostrikov.pet_twitter.entity

class User {
    String id
    String nickName
    String firstName
    String secondName
    String email
    String password
    String role
    String phoneNumber
    Set<String> subscriptions
    Set<String> favoritePosts


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
