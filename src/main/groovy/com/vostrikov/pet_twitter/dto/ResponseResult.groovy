package com.vostrikov.pet_twitter.dto

class ResponseResult {
    String message
    Object result

    ResponseResult(String message) {
        this.message = message
    }

    ResponseResult(String message, Object result) {
        this.message = message
        this.result = result
    }
}
