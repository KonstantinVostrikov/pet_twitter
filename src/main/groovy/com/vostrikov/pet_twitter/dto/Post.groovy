package com.vostrikov.pet_twitter.dto

import java.time.LocalDateTime

class Post {
    String id
    String userId
    String content
    LocalDateTime createdAt
    LocalDateTime modifiedAt
    List<String> hashtags
    Map<String,Boolean> likes
    List<Comment> comments


    @Override
    String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", hashtags=" + hashtags +
                '}'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        Post post = (Post) o

        if (content != post.content) return false
        if (hashtags != post.hashtags) return false
        if (id != post.id) return false
        if (userId != post.userId) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (userId != null ? userId.hashCode() : 0)
        result = 31 * result + (content != null ? content.hashCode() : 0)
        result = 31 * result + (hashtags != null ? hashtags.hashCode() : 0)
        return result
    }
}

