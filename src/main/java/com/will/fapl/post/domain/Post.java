package com.will.fapl.post.domain;

import static java.util.stream.Collectors.toList;

import com.will.fapl.comment.domain.Comment;
import com.will.fapl.common.model.BaseEntity;
import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.image.domain.PostImage;
import com.will.fapl.image.domain.PostImageList;
import com.will.fapl.member.domain.Member;
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
    @JoinColumn(name = "member_id")
    private Member member;

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
    private List<PostLikeMember> likedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostDislikeMember> dislikedMembers = new ArrayList<>();

    @Builder
    public Post(Member member, String content, Long likeCnt, Long dislikeCnt, List<Comment> comments,
                    List<String> postImages, List<PostLikeMember> postLikeMembers,
                        List<PostDislikeMember> postDislikeMembers, List<Hashtag> hashtags) {
        this.member = member;
        this.member.addPost(this);
        this.content = content;
        this.hashTagList = new PostHashTagList(convertToHashtags(hashtags));
        this.likeCnt = likeCnt;
        this.dislikeCnt = dislikeCnt;
        this.postImageList = new PostImageList(convertToPostImages(postImages));
        this.comments = comments;
        this.likedMembers = postLikeMembers;
        this.dislikedMembers = postDislikeMembers;
    }

    private List<PostHashtag> convertToHashtags(List<Hashtag> hashtags) {
        return hashtags.stream()
            .map(hashtag -> new PostHashtag(this, hashtag))
            .toList();
    }


    private List<PostImage> convertToPostImages(List<String> imageUrls) {
        return imageUrls.stream()
            .map(imageUrl -> new PostImage(this, imageUrl))
            .collect(toList());
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changePostImageList(List<String> imageUrls) {
        this.postImageList.changePostImageList(convertToPostImages(imageUrls));
    }

    public void changeHashtagList(List<Hashtag> hashtags) {
        this.hashTagList.changePostHashtags(convertToHashtags(hashtags));
    }

    public boolean isWrittenBy(Member member) {
        return this.member.isSame(member);
    }
}
