package com.vostrikov.pet_twitter.exceptions.user

class UserAlreadyExistException extends RuntimeException{

    UserAlreadyExistException() {
        super("User already exist")
    }

    UserAlreadyExistException(String message) {
        super(message)
    }
}
