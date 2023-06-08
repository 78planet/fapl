package com.will.fapl.member.exception;

import static java.lang.String.format;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;

public class NotFoundMemberException  extends BusinessException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 회원입니다. 회원 번호: {0}";

    public NotFoundMemberException(ErrorCode errorCode, Long id) {
        super(format(ERROR_MESSAGE_FORMAT, id), errorCode);
    }
}
