package com.will.fapl.auth.exception;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
