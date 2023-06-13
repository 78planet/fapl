package com.will.fapl.hashtag.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.hashtag.domain.HashtagRepository;
import com.will.fapl.util.IntegrationTest;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HashtagServiceTest extends IntegrationTest {

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Test
    void createHashtag_test() {
        // given
        List<String> hashtagStrs = List.of(
            "h1",
            "h2",
            "h3"
        );
        hashtagRepository.saveAll(hashtagStrs.stream().map(Hashtag::new).toList());

        // when
        List<String> newHashtagStrs = List.of(
            "h1",
            "h3",
            "h4",
            "h5"
        );
        List<Hashtag> hashtagList = hashtagService.createHashtag(newHashtagStrs);

        // then
        assertAll(
            () -> assertThat(hashtagList.get(0).getId()).isEqualTo(1),
            () -> assertThat(hashtagList.get(1).getId()).isEqualTo(3),
            () -> assertThat(hashtagList.get(2).getId()).isEqualTo(4),
            () -> assertThat(hashtagList.get(3).getId()).isEqualTo(5)
        );
    }
}
