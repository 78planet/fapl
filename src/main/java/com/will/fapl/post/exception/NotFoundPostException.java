package com.will.fapl.post.exception;

import static java.lang.String.format;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;

public class NotFoundPostException extends BusinessException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 게시글입니다. 게시글 번호: {0}";

    public NotFoundPostException(ErrorCode errorCode, Long postId) {
        super(format(ERROR_MESSAGE_FORMAT, postId), errorCode);
    }
}
