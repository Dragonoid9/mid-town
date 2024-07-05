package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.entity.Comment;
import com.rac.ktm.midtown.entity.Reply;
import com.rac.ktm.midtown.repository.CommentRepository;
import com.rac.ktm.midtown.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void addReply(Reply reply) {
        replyRepository.save(reply);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    public Long findPostIdByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));
        return comment.getPost().getId();
    }

    public Long findPostIdByReplyId(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("Invalid reply Id:" + replyId));
        return reply.getComment().getPost().getId();
    }

}
