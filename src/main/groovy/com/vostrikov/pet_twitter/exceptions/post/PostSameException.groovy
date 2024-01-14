package com.vostrikov.pet_twitter.exceptions.post

class PostSameException extends RuntimeException{

    PostSameException() {
        super("Post is the identical - nothing to change")
    }

    PostSameException(String message) {
        super(message)
    }
}
