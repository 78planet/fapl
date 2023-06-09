package com.will.fapl.member.exception;

import com.will.fapl.common.exception.BusinessException;
import com.will.fapl.common.exception.ErrorCode;
import java.text.MessageFormat;

public class DuplicateEmailException extends BusinessException {

    private static final String ERROR_MESSAGE_FORMAT = "중복된 이메일입니다. 현재 이메일 : {0}";

    public DuplicateEmailException(String email, ErrorCode errorCode) {
        super(MessageFormat.format(ERROR_MESSAGE_FORMAT, email), errorCode);
    }
}
