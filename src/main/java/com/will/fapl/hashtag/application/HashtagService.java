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
        List<Hashtag> existingHashtags = hashtagRepository.findAllByNameIn(hashtags);
        List<Hashtag> newHashtags = hashtags.stream()
            .filter(hashtag -> existingHashtags.stream().noneMatch(h -> h.getName().equals(hashtag)))
            .map(Hashtag::new)
            .toList();

        if (!newHashtags.isEmpty()) {
            hashtagRepository.saveAll(newHashtags);
        }

        existingHashtags.addAll(newHashtags);
        return existingHashtags;
    }
}
