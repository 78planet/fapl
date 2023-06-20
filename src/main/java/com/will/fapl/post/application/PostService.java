package com.will.fapl.post.application;

import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.application.dto.response.PostSearchResponse;
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

    public List<PostSearchResponse> searchPostsByHashtag(PostFilterCondition postFilterCondition) {
        List<Post> posts = postRepository.findPostsByHashtag(postFilterCondition);
        return PostSearchResponse.from(posts);
    }

    @Transactional
    public Post modifyPost(Long postId, List<Hashtag> hashtagList, EditPostRequest editPostRequest) {
        Post post = getPostById(postId);

        post.changeContent(editPostRequest.getContent());
        post.changePostImageList(editPostRequest.getImageUrls());
        post.changeHashtagList(hashtagList);

        return post;
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new NotFoundPostException(ErrorCode.NOT_FOUND_POST, id));
    }

    @Transactional
    public void removePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
