package com.will.fapl.member.exception;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;

public class LoginFailedException extends BusinessException {

    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
