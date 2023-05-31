package com.will.fapl.post.domain;

import static java.util.stream.Collectors.toList;

import com.will.fapl.comment.domain.Comment;
import com.will.fapl.common.model.BaseEntity;
import com.will.fapl.image.domain.PostImage;
import com.will.fapl.image.domain.PostImageList;
import com.will.fapl.user.domain.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private Long likeCnt;

    private Long dislikeCnt;

    @Embedded
    private PostImageList postImageList;

    @Embedded
    private PostHashTagList hashTagList;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLikeUser> likedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostDislikeUser> dislikedUsers = new ArrayList<>();

    @Builder
    public Post(User user, String content, Long likeCnt, Long dislikeCnt, List<Comment> comments,
                    List<String> postImages, List<PostLikeUser> postLikeUsers, List<PostDislikeUser> postDislikeUsers) {
        this.user = user;
        this.user.addPost(this);
        this.content = content;
        this.likeCnt = likeCnt;
        this.dislikeCnt = dislikeCnt;
        this.postImageList = new PostImageList(convertToPostImages(postImages));
        this.comments = comments;
        this.likedUsers = postLikeUsers;
        this.dislikedUsers = postDislikeUsers;
    }

    private List<PostImage> convertToPostImages(List<String> imageUrls) {
        return imageUrls.stream()
            .map(imageUrl -> new PostImage(this, imageUrl))
            .collect(toList());
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
