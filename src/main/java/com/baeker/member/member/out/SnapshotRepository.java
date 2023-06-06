package com.baeker.member.member.out;

import com.baeker.member.member.domain.entity.MemberSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnapshotRepository extends JpaRepository<MemberSnapshot, Long> {
}
