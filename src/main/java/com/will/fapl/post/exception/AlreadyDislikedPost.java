package com.will.fapl.post.exception;

import static java.lang.String.format;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;

public class AlreadyDislikedPost extends BusinessException {

    private static final String MESSAGE_FORMAT = "이미 싫어요를 누른 게시글입니다. 멤버 : {0}, 게시물 : {1}";

    public AlreadyDislikedPost(Long memberId, Long postId, ErrorCode errorCode) {
        super(format(MESSAGE_FORMAT, memberId, postId), errorCode);
    }
}
