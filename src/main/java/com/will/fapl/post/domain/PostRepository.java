package com.will.fapl.post.domain;

import com.will.fapl.post.infra.PostQueryRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :postId")
    Optional<Post> findPostWithFetchById(Long postId);
}
