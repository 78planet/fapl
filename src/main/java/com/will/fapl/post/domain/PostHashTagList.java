package com.will.fapl.post.domain;

import static com.google.common.base.Preconditions.checkArgument;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostHashTagList {

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<PostHashtag> postHashtags = new HashSet<>();

    public PostHashTagList(List<PostHashtag> postHashtags) {
        this.postHashtags = new HashSet<>(postHashtags);
    }

    public void changePostHashtags(List<PostHashtag> postHashtagsToAdd) {
        postHashtags.clear();
        postHashtags.addAll(postHashtagsToAdd);
    }
}
