package com.will.fapl.post.domain;

import com.will.fapl.comment.domain.Comment;
import com.will.fapl.common.model.BaseEntity;
import com.will.fapl.hashtag.domain.HashTagList;
import com.will.fapl.image.domain.PostImageList;
import com.will.fapl.like.domain.PostDislikeUser;
import com.will.fapl.like.domain.PostLikeUser;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private Integer like_cnt;

    private Integer dislike_cnt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Embedded
    private PostImageList postImages;

    @Embedded
    private HashTagList hashTags;

    @OneToMany(mappedBy = "post")
    private List<PostLikeUser> likedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostDislikeUser> dislikedUsers = new ArrayList<>();

    public void changeContent(String content) {
        this.content = content;
    }
}
