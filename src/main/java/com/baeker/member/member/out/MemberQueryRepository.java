package com.baeker.member.member.out;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.QMember;
import com.baeker.member.member.in.resDto.QSchedulerResDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory query;

    public MemberQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- 백준 연동한 모든 회원 조회 --//
    public List<SchedulerResDto> findAllConBJ() {
        QMember m = QMember.member;

        return query
                .select(new QSchedulerResDto(m.id, m.baekJoonName, m.bronze, m.silver, m.gold, m.diamond, m.ruby, m.platinum))
                .from(m)
                .where(m.baekJoonName.isNotNull())
                .fetch();
    }
}

