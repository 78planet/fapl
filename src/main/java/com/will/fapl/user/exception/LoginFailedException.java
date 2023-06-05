package com.will.fapl.user.exception;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;

public class LoginFailedException extends BusinessException {

    public LoginFailedException(String format, ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
