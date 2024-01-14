package com.vostrikov.pet_twitter.exceptions.comment

class CommentAlreadyExistException extends RuntimeException{

    CommentAlreadyExistException() {
        super("Comment already exist")
    }

    CommentAlreadyExistException(String message) {
        super(message)
    }
}
