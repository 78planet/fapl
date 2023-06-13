package com.will.fapl.post.presentation;

import static com.will.fapl.common.util.LocationUriUtil.createLocationUri;

import com.will.fapl.auth.aop.Login;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.common.model.ApiResponse;
import com.will.fapl.post.application.PostFacade;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.response.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPost(@Valid @RequestBody CreatePostRequest createPostRequest,
                                                        @Login LoginMember loginMember) {
        Long postId = postFacade.createPost(loginMember, createPostRequest);
        return ResponseEntity.created(createLocationUri(postId)).build();
    }

}
