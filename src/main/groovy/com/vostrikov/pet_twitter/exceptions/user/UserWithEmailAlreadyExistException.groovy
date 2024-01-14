package com.vostrikov.pet_twitter.exceptions.user

class UserWithEmailAlreadyExistException extends UserAlreadyExistException{

    UserWithEmailAlreadyExistException(String message) {
        super(message)
    }
}
