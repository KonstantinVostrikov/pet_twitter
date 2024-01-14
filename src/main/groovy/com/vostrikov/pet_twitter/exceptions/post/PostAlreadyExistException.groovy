package com.vostrikov.pet_twitter.exceptions.post

class PostAlreadyExistException extends RuntimeException{

    PostAlreadyExistException() {
        super("Post already exist")
    }

    PostAlreadyExistException(String message) {
        super(message)
    }
}
