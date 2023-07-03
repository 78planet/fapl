package com.will.fapl.post.exception;

import static java.lang.String.format;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;

public class PostNotBelongToCoupleException extends BusinessException {

    private static final String MESSAGE_FORMAT = "해당하는 사용자의 게시물이 아닙니다. 멤버 : {0}, 게시물 : {1}";

    public PostNotBelongToCoupleException(Long memberId, Long postId, ErrorCode errorCode) {
        super(format(MESSAGE_FORMAT, memberId, postId), errorCode);
    }
}
