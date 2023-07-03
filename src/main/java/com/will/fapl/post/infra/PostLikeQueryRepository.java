package com.will.fapl.post.infra;

import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.domain.Post;
import java.util.List;

public interface PostLikeQueryRepository {

    List<Post> findPostsByLiked(Member member, PostFilterCondition postFilterCondition);
}
