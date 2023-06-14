package com.will.fapl.member.domain;

import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.common.model.BaseEntity;
import com.will.fapl.member.exception.LoginFailedException;
import com.will.fapl.post.domain.PostLikeMember;
import com.will.fapl.point.domain.Point;
import com.will.fapl.post.domain.Post;
import com.will.fapl.member.domain.converter.GradeConverter;
import com.will.fapl.member.domain.vo.Email;
import com.will.fapl.member.domain.vo.NickName;
import com.will.fapl.member.domain.vo.Password;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private NickName nickName;

    @Convert(converter = GradeConverter.class)
    private Grade grade;

    private String profileImage;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<PostLikeMember> postLikeMembers = new ArrayList<>();

    @Embedded
    private Point point;

    @Builder
    public Member(Email email, Password password, NickName nickName, String imageUrl, Grade grade, Point point) {
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

    public void checkPassword(PasswordEncoder passwordEncoder, String password) {
        if (!this.password.isSamePassword(passwordEncoder, password)) {
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }
    }

    public void changeNickName(String nickName) {
        this.nickName = new NickName(nickName);
    }

    public boolean isSame(Member member) {
        return this.id.equals(member.id);
    }
}
