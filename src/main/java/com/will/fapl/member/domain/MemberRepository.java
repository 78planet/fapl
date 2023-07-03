package com.will.fapl.member.domain;

import com.will.fapl.member.domain.vo.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailValue(String email);

    Optional<Member> findByEmail(Email email);

    @Query("SELECT m FROM Member m "
        + "LEFT JOIN FETCH m.postLikeMembers "
        + "LEFT JOIN FETCH m.postDislikeMembers WHERE m.id = :memberId")
    Optional<Member> findMemberWithFetchById(Long memberId);
}
