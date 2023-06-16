package com.will.fapl.comment.presentation;

import static com.will.fapl.common.util.LocationUriUtil.createLocationUri;

import com.will.fapl.auth.aop.Login;
import com.will.fapl.auth.aop.LoginRequired;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.comment.application.CommentFacade;
import com.will.fapl.comment.application.dto.request.CreateCommentRequest;
import com.will.fapl.common.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comment", description = "Comment APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {

    private final CommentFacade commentFacade;

    @Operation(summary = "댓글 생성", description = "댓글 생성 API입니다.")
    @LoginRequired
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest,
                                                            @Login LoginMember loginMember) {
        commentFacade.createComment(loginMember, createCommentRequest);
        return ResponseEntity.created(createLocationUri(createCommentRequest.getPostId())).build();
    }

}
