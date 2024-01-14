package com.vostrikov.pet_twitter.exceptions.user

class UserNotExistException extends RuntimeException{

    UserNotExistException() {
        super("User not exist")
    }

    UserNotExistException(String message) {
        super(message)
    }
}
