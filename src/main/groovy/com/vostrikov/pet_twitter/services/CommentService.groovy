package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.Comment

interface CommentService {

    Comment addComment(Comment comment)

    List<Comment> fetchComments(String postId)
}