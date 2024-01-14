package com.vostrikov.pet_twitter.exceptions.subscription

class SubscriptionException extends RuntimeException{

    SubscriptionException() {
        super("Subscription are wrong - from or to corrupted")
    }

    SubscriptionException(String message) {
        super(message)
    }
}
