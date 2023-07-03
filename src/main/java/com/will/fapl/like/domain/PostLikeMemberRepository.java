package com.will.fapl.like.domain;

import com.will.fapl.member.domain.Member;
import com.will.fapl.post.domain.Post;
import com.will.fapl.post.infra.PostLikeQueryRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeMemberRepository extends JpaRepository<PostLikeMember, Long>, PostLikeQueryRepository {

    Long countByMemberAndPost(Member member, Post post);

    void deletePostLikeMemberByMemberAndPost(Member member, Post post);
}
