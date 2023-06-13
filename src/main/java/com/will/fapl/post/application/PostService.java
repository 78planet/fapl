package com.will.fapl.post.application;

import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.domain.Post;
import com.will.fapl.post.domain.PostRepository;
import com.will.fapl.post.exception.NotFoundPostException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(Member member, List<Hashtag> hashtags, CreatePostRequest createPostRequest) {
        Post post = createPostRequest.toPost(member, hashtags);
        return postRepository.save(post);
    }

    public Post getPostWithFetchById(Long postId) {
        return postRepository.findPostWithFetchById(postId)
            .orElseThrow(() -> new NotFoundPostException(ErrorCode.NOT_FOUND_POST, postId));
    }
}
