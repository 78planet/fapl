package com.will.fapl.hashtag.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    List<Hashtag> findAllByHashtagIn(List<String> hashtags);
}
