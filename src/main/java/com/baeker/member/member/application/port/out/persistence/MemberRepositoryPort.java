package com.baeker.member.member.application.port.out.persistence;

import com.baeker.member.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepositoryPort extends JpaRepository<Member, Long>, MemberQueryRepository {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByBaekJoonName(String BaekJoonName);

    Page<Member> findAll(Pageable pageable);
}
