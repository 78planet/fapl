package com.will.fapl.post.presentation;

import static com.will.fapl.common.util.LocationUriUtil.createLocationUri;

import com.will.fapl.auth.aop.Login;
import com.will.fapl.auth.aop.LoginRequired;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.common.model.ApiResponse;
import com.will.fapl.post.application.PostFacade;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.application.dto.response.PostResponse;
import com.will.fapl.post.application.dto.response.PostSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post", description = "Post APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/posts")
public class PostController {

    private final PostFacade postFacade;

    @Operation(summary = "게시글 생성", description = "게시글 생성 API입니다.")
    @LoginRequired
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPost(@Valid @RequestBody CreatePostRequest createPostRequest,
                                                        @Login LoginMember loginMember) {
        Long postId = postFacade.createPost(loginMember, createPostRequest);
        return ResponseEntity.created(createLocationUri(postId)).build();
    }

    @Operation(summary = "게시물 단건 조회", description = "게시물 단건 조회 API입니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> showPost(@PathVariable("postId") Long postId) {
        PostResponse postResponse = postFacade.getPost(postId);
        return ResponseEntity.ok(new ApiResponse<>(postResponse));
    }

    @Operation(summary = "게시글 해시태그 검색", description = "게시물 해시태그 검색 API입니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> getPostsByHashtag(PostFilterCondition postFilterCondition) {
        List<PostSearchResponse> postSearchResponses = postFacade.searchPosts(postFilterCondition);
        return ResponseEntity.ok(new ApiResponse<>(postSearchResponses));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API입니다.")
    @LoginRequired
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> modifyPost(@Valid @RequestBody EditPostRequest editPostRequest,
                                                        @Login LoginMember loginMember,
                                                        @PathVariable("postId") Long postId) {
        postFacade.modifyPost(loginMember, postId, editPostRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API입니다.")
    @LoginRequired
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@Login LoginMember loginMember,
                                                        @PathVariable("postId") Long postId) {
        postFacade.removePost(loginMember.getId(), postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 좋아요", description = "게시글 좋아요 API입니다.")
    @LoginRequired
    @PostMapping("/like/{postId}")
    public ResponseEntity<ApiResponse<Void>> likePost(@Login LoginMember loginMember,
                                                      @PathVariable("postId") Long postId) {
        postFacade.likePost(loginMember.getId(), postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요 취소 API입니다.")
    @LoginRequired
    @PostMapping("/like/cancel/{postId}")
    public ResponseEntity<ApiResponse<Void>> cancelLikePost(@Login LoginMember loginMember,
                                                            @PathVariable("postId") Long postId) {
        postFacade.cancelLike(loginMember.getId(), postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 싫어요", description = "게시글 싫어요 API입니다.")
    @LoginRequired
    @PostMapping("/dislike/{postId}")
    public ResponseEntity<ApiResponse<Void>> dislikePost(@Login LoginMember loginMember,
                                                         @PathVariable("postId") Long postId) {
        postFacade.dislikePost(loginMember.getId(), postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 싫어요 취소", description = "게시글 싫어요 취소 API입니다.")
    @LoginRequired
    @PostMapping("/dislike/cancel/{postId}")
    public ResponseEntity<ApiResponse<Void>> cancelDislikePost(@Login LoginMember loginMember,
                                                               @PathVariable("postId") Long postId) {
        postFacade.cancelDislike(loginMember.getId(), postId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "좋아요 게시글 리스트", description = "좋아요 게시글 리스트 API입니다.")
    @LoginRequired
    @GetMapping("/like")
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> likePostList(@Login LoginMember loginMember,
                                                                                PostFilterCondition postFilterCondition) {
        List<PostSearchResponse> postSearchResponses = postFacade.getLikedPostList(loginMember.getId(), postFilterCondition);
        return ResponseEntity.ok().build();
    }

}
