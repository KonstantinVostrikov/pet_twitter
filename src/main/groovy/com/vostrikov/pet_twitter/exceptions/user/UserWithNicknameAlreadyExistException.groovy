package com.vostrikov.pet_twitter.exceptions.user

class UserWithNicknameAlreadyExistException extends UserAlreadyExistException{

    UserWithNicknameAlreadyExistException() {
        super("User with such nickname already exist")
    }

    UserWithNicknameAlreadyExistException(String message) {
        super(message)
    }
}
