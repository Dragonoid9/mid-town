package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByCommentId(Long commentId);
}
