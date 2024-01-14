package com.vostrikov.pet_twitter.exceptions.post

class PostNotExistException extends RuntimeException{

    PostNotExistException() {
        super("Post doesn't exist")
    }

    PostNotExistException(String message) {
        super(message)
    }
}
