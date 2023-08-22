package com.baeker.member.member.in.resDto;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.domain.entity.QMemberSnapshot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SnapshotQueryRepository {

    private final JPAQueryFactory query;
    private QMemberSnapshot snapshot = QMemberSnapshot.memberSnapshot;

    public SnapshotQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- find by member / 삭제 예정--//
    public List<MemberSnapshot> findByMemberId(Member member) {

        return query
                .selectFrom(snapshot)
                .where(snapshot.member.eq(member))
                .fetch();
    }
}
