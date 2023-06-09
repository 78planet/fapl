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
    private Set<PostHashtag> postTags = new HashSet<>();

    public PostHashTagList(List<PostHashtag> postTags) {
        this.postTags = new HashSet<>(postTags);
    }

    public void changePostHashtags(List<PostHashtag> postTagsToAdd) {
        postTags.clear();
        postTags.addAll(postTagsToAdd);
    }
}
