package com.will.fapl.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.will.fapl.point.domain.Point;
import com.will.fapl.member.domain.Grade;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.vo.Email;
import com.will.fapl.member.domain.vo.NickName;
import com.will.fapl.member.domain.vo.Password;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PostTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("post 생성 성공")
    @Test
    void createPost_success() {
        // given
        // when
        List<String> imageUrls = List.of(
            "imageUrl1",
            "imageUrl2",
            "imageUrl3",
            "imageUrl4",
            "imageUrl5",
            "imageUrl6"
        );

        Post thisIsContent = Post.builder()
            .member(getUser())
            .content("this is content #sdaf")
            .hashtags(null)
            .likeCnt(123L)
            .dislikeCnt(2L)
            .postImages(imageUrls)
            .build();

        // then
        assertThat(thisIsContent).isNotNull();
    }

    @DisplayName("post 생성 실패 - Image 0개")
    @Test
    void createPost_image_fail() {
        // given
        // when
        List<String> imageUrls = List.of();

        // then
        assertThatThrownBy(() -> getPost(imageUrls))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미지는 1개 이상 등록해야합니다");
    }

    @DisplayName("post 생성 실패 - Image 10개 초과")
    @Test
    void createPost_image_fail2() {
        // given
        // when
        List<String> imageUrls = List.of(
            "image1",
            "image2",
            "image3",
            "image4",
            "image5",
            "image6",
            "image7",
            "image8",
            "image9",
            "image10",
            "image11"
        );

        // then
        assertThatThrownBy(() -> getPost(imageUrls))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미지는 최대 10개 등록 가능합니다");
    }

    private Post getPost(List<String> imageUrls) {
        return Post.builder()
            .member(getUser())
            .content("this is content")
            .likeCnt(123L)
            .dislikeCnt(2L)
            .postImages(imageUrls)
            .build();
    }

    private Member getUser() {
        String email = "test@mail.com";
        String password = "!@Password123";
        String nickName = "qr123";

        return Member.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, password))
            .nickName(new NickName(nickName))
            .grade(Grade.BRONZE)
            .point(new Point(0L))
            .build();
    }

}
