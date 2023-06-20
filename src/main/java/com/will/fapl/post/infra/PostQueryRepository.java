package com.will.fapl.post.infra;

import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.domain.Post;
import java.util.List;

public interface PostQueryRepository {

    List<Post> findPostsByHashtag(PostFilterCondition postFilterCondition);
}
