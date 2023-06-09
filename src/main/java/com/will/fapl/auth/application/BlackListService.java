package com.will.fapl.auth.application;

import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.domain.BlackList;
import com.will.fapl.auth.domain.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlackListService {

    private final BlackListRepository blackListRepository;

    @Transactional
    public void saveBlackList(TokenDto tokenDto) {
        BlackList blackList = new BlackList(tokenDto.getValue(), tokenDto.getExpiredTime());
        blackListRepository.save(blackList);
    }

    public boolean isBlackList(String accessToken) {
        return blackListRepository.existsById(accessToken);
    }
}
