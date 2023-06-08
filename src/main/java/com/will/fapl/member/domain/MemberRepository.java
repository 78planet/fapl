package com.will.fapl.member.domain;

import com.will.fapl.member.domain.vo.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailValue(String email);

    Optional<Member> findByEmail(Email email);
}
