package com.will.fapl.hashtag.application;

import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.hashtag.domain.HashtagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    @Transactional
    public List<Hashtag> createHashtag(List<String> hashtags) {
        return hashtagRepository.saveAll(toHashtags(hashtags));
    }

    private List<Hashtag> toHashtags(List<String> hashtags) {
        return hashtags.stream()
            .map(Hashtag::new)
            .toList();
    }
}
