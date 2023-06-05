package com.baeker.member.member.out;

import com.baeker.member.member.domain.entity.MemberSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapshotRepository extends JpaRepository<MemberSnapshot, Long> {
}
