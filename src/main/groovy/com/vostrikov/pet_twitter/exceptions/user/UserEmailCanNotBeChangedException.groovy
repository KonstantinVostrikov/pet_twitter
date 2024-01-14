package com.vostrikov.pet_twitter.exceptions.user

class UserEmailCanNotBeChangedException extends RuntimeException{

    UserEmailCanNotBeChangedException() {
        super("User email can not be changed")
    }

    UserEmailCanNotBeChangedException(String message) {
        super(message)
    }
}
