package com.vostrikov.pet_twitter.exceptions.post

class MissingPostIdException extends RuntimeException{

    MissingPostIdException() {
        super("postId can't by empty to find post")
    }

    MissingPostIdException(String message) {
        super(message)
    }
}
