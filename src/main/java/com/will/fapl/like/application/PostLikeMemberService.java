package com.will.fapl.like.application;

import com.will.fapl.like.domain.PostLikeMember;
import com.will.fapl.like.domain.PostLikeMemberRepository;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.application.dto.response.PostSearchResponse;
import com.will.fapl.post.domain.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeMemberService {

    private final PostLikeMemberRepository postLikeMemberRepository;

    public boolean isLikePost(Member member, Post post) {
        Long cnt = postLikeMemberRepository.countByMemberAndPost(member, post);
        return cnt > 0;
    }

    @Transactional
    public void addPost(Member member, Post post) {
        postLikeMemberRepository.save(new PostLikeMember(member, post));
    }

    @Transactional
    public void cancelLike(Member member, Post post) {
        postLikeMemberRepository.deletePostLikeMemberByMemberAndPost(member, post);
    }

    public List<PostSearchResponse> getLikedPostList(Member  member, PostFilterCondition postFilterCondition) {
        List<Post> posts = postLikeMemberRepository.findPostsByLiked(member, postFilterCondition);
        return PostSearchResponse.from(posts);
    }
}
