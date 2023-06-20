package com.will.fapl.post.infra;

import static com.will.fapl.hashtag.domain.QHashtag.hashtag;
import static com.will.fapl.image.domain.QPostImage.postImage;
import static com.will.fapl.post.domain.QPost.post;
import static com.will.fapl.post.domain.QPostHashtag.postHashtag;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.domain.Post;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostsByHashtag(PostFilterCondition postFilterCondition) {
        List<Long> postIds = jpaQueryFactory.select(postHashtag.post.id)
            .from(postHashtag)
            .innerJoin(postHashtag.hashtag, hashtag)
            .distinct()
            .where(
                lastPostIdLo(postFilterCondition.getLastPostId()),
                hashtagEq(postFilterCondition.getHashtag())
            )
            .groupBy(post.id)
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

    private BooleanExpression hashtagEq(String inputHashtag) {
        return Objects.isNull(inputHashtag) ? null : hashtag.name.eq(inputHashtag);
    }
}
