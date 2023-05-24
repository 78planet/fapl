package com.will.fapl.image.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageList {

    public static final int THUMBNAIL_INDEX = 0;
    private static final int POST_IMAGES_MIN_SIZE = 1;
    private static final int POST_IMAGES_MAX_SIZE = 10;
    private static final String POST_IMAGES_MIN_SIZE_MESSAGE = "이미지는 1개 이상 등록해야합니다";
    private static final String POST_IMAGES_MAX_SIZE_MESSAGE = "이미지는 최대 10개 등록 가능합니다";

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    public PostImageList(List<PostImage> postImages) {
        checkArgument(postImages.size() >= POST_IMAGES_MIN_SIZE, POST_IMAGES_MIN_SIZE_MESSAGE);
        checkArgument(postImages.size() <= POST_IMAGES_MAX_SIZE, POST_IMAGES_MAX_SIZE_MESSAGE);
        this.postImages = postImages;
    }

    public String getThumbnailUrl() {
        return postImages.get(THUMBNAIL_INDEX).getImageUrl();
    }

    public List<String> getImageUrls() {
        return postImages.stream()
            .map(PostImage::getImageUrl)
            .collect(toList());
    }

    public void changePostImageList(List<PostImage> postImagesToAdd) {
        checkArgument(postImages.size() >= POST_IMAGES_MIN_SIZE, POST_IMAGES_MIN_SIZE_MESSAGE);
        checkArgument(postImages.size() <= POST_IMAGES_MAX_SIZE, POST_IMAGES_MAX_SIZE_MESSAGE);
        postImages.clear();
        postImages.addAll(postImagesToAdd);
    }
}
