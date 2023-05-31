package com.will.fapl.user.domain;

import com.will.fapl.common.model.BaseEntity;
import com.will.fapl.post.domain.PostLikeUser;
import com.will.fapl.point.domain.Point;
import com.will.fapl.post.domain.Post;
import com.will.fapl.user.domain.vo.Email;
import com.will.fapl.user.domain.vo.NickName;
import com.will.fapl.user.domain.vo.Password;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private NickName nickName;

    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    private String profileImage;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<PostLikeUser> postLikeUsers = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "point_id")
    private Point point;

    @Builder
    public User(Email email, Password password, NickName nickName, String imageUrl, Grade grade, Point point) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.profileImage = imageUrl;
        this.grade = grade;
        this.point = point;
    }

    public void changeNickname(String nickName) {
        this.nickName = new NickName(nickName);
    }

    public void changeGrade(Grade grade) {
        this.grade = grade;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void changePoint(Point point) {
        this.point = point;
    }
}
