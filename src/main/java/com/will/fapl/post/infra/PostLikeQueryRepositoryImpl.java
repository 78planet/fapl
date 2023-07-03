package com.will.fapl.post.infra;

import static com.will.fapl.image.domain.QPostImage.postImage;
import static com.will.fapl.like.domain.QPostLikeMember.postLikeMember;
import static com.will.fapl.post.domain.QPost.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.domain.Post;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostLikeQueryRepositoryImpl implements PostLikeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostsByLiked(Member member, PostFilterCondition postFilterCondition) {
        List<Long> postIds = jpaQueryFactory.select(postLikeMember.post.id)
            .from(postLikeMember)
            .where(
                lastPostIdLo(postFilterCondition.getLastPostId()),
                postLikeMember.member.eq(member)
            )
            .orderBy(post.id.desc())
            .limit(postFilterCondition.getPaginationSize())
            .fetch();

        return jpaQueryFactory.selectFrom(post)
            .innerJoin(post.postImageList.postImages, postImage)
            .fetchJoin()
            .where(
                post.id.in(postIds)
            )
            .distinct()
            .orderBy(post.id.desc())
            .fetch();
    }

    private BooleanExpression lastPostIdLo(Long lastPostId) {
        return Objects.isNull(lastPostId) ? null : post.id.lt(lastPostId);
    }
}
