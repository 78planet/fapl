package com.will.fapl.like.application;

import com.will.fapl.like.domain.PostDislikeMember;
import com.will.fapl.like.domain.PostDislikeMemberRepository;
import com.will.fapl.like.domain.PostLikeMemberRepository;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostDislikeMemberService {

    private final PostDislikeMemberRepository postDislikeMemberRepository;

    public boolean isDislikePost(Member member, Post post) {
        Long cnt = postDislikeMemberRepository.countByMemberAndPost(member, post);
        return cnt > 0;
    }

    @Transactional
    public void addPost(Member member, Post post) {
        postDislikeMemberRepository.save(new PostDislikeMember(member, post));
    }

    @Transactional
    public void cancelDislike(Member member, Post post) {
        postDislikeMemberRepository.deletePostDislikeMemberByMemberAndPost(member, post);
    }
}
