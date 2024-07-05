package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.entity.Comment;
import com.rac.ktm.midtown.entity.Post;
import com.rac.ktm.midtown.entity.Reply;
import com.rac.ktm.midtown.service.CommentService;
import com.rac.ktm.midtown.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rac/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService; // Add PostService to fetch the Post entity

    @PostMapping("/add")
    public String addComment(@RequestParam("commentText") String commentText, @RequestParam("postId") Long postId, HttpSession session) {
        Post post = postService.findById(postId);
        if (post == null) {
            // Handle case when post is not found
            return "redirect:/rac/homePage";
        }

        Comment comment = new Comment();
        comment.setText(commentText);
        comment.setUsername((String) session.getAttribute("user"));
        comment.setPost(post);  // Use the fetched post entity
        commentService.addComment(comment);
        return "redirect:/rac/post/detail/" + postId;
    }

    @PostMapping("/reply")
    public String addReply(@RequestParam("replyText") String replyText, @RequestParam("commentId") Long commentId, HttpSession session) {
        Reply reply = new Reply();
        reply.setText(replyText);
        reply.setUsername((String) session.getAttribute("user"));
        reply.setComment(new Comment(commentId));  // Assuming you have a Comment constructor with commentId
        commentService.addReply(reply);
        return "redirect:/rac/post/detail/" + commentService.findPostIdByCommentId(commentId);
    }

    @PostMapping("/delete")
    public String deleteComment(@RequestParam("commentId") Long commentId) {
        Long postId = commentService.findPostIdByCommentId(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/rac/post/detail/" + postId;
    }
    @PostMapping("/reply/delete")
        public String deleteReply(@RequestParam("replyId")Long replyId){
        Long postId = commentService.findPostIdByReplyId(replyId);
        commentService.deleteReply(replyId);
        return "redirect:/rac/post/detail/" + postId;
    }
}
