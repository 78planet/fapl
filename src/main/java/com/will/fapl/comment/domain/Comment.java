package com.will.fapl.comment.domain;

import com.will.fapl.post.domain.Post;
import com.will.fapl.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    private Long hierarchy;

    private Long groupId;

    private Long orderNum;

    public Comment(Post post, Member member, String content, Long hierarchy, Long groupId,
        Long orderNum) {
        this.post = post;
        this.member = member;
        this.content = content;
        this.hierarchy = hierarchy;
        this.groupId = groupId;
        this.orderNum = orderNum;
    }
}
