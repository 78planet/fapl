package com.will.fapl.user.application;

import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.point.domain.Point;
import com.will.fapl.user.application.dto.SignupRequest;
import com.will.fapl.user.domain.Grade;
import com.will.fapl.user.domain.User;
import com.will.fapl.user.domain.UserRepository;
import com.will.fapl.user.domain.vo.Email;
import com.will.fapl.user.domain.vo.NickName;
import com.will.fapl.user.domain.vo.Password;
import com.will.fapl.user.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        validateExistEmail(email);

        User user = User.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, signupRequest.getPassword()))
            .nickName(new NickName(signupRequest.getNickName()))
            .grade(Grade.BRONZE)
            .point(new Point(0L))
            .build();
        return userRepository.save(user).getId();
    }

    private void validateExistEmail(String email) {
        if (userRepository.existsByEmailValue(email)) {
            throw new DuplicateEmailException(email, ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
