package com.will.fapl.like.domain;

import com.will.fapl.member.domain.Member;
import com.will.fapl.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDislikeMemberRepository extends JpaRepository<PostDislikeMember, Long> {

    Long countByMemberAndPost(Member member, Post post);

    void deletePostDislikeMemberByMemberAndPost(Member member, Post post);
}
