package com.baeker.member.member.out;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.QMember;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.QMemberDto;
import com.baeker.member.member.in.resDto.QSchedulerResDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory query;
    private QMember m = QMember.member;

    public MemberQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- 백준 연동한 모든 회원 조회 --//
    public List<SchedulerResDto> findAllConBJ() {
        return query
                .select(new QSchedulerResDto(m.id, m.baekJoonName, m.bronze, m.silver, m.gold, m.diamond, m.ruby, m.platinum))
                .from(m)
                .where(m.baekJoonName.isNotNull())
                .fetch();
    }

    //-- id list 로 member list 조회 --//
    public List<MemberDto> findByMemberList(List<Long> memberIds, String status) {
        List<MemberDto> result = query
                .select(new QMemberDto(
                        m.id,
                        m.createDate,
                        m.modifyDate,
                        m.bronze,
                        m.silver,
                        m.gold,
                        m.diamond,
                        m.ruby,
                        m.platinum,
                        m.username,
                        m.nickname,
                        m.baekJoonName,
                        m.about,
                        m.profileImg,
                        m.provider,
                        m.email,
                        m.token,
                        m.newMember,
                        m.lastSolvedProblemId,
                        m.ranking
                ))
                .from(m)
                .where(m.id.in(memberIds))
                .fetch();

        for (MemberDto dto : result) dto.setStatus(status);
        return result;
    }

    //-- 문제 해결 순위로 리스트 조회 --//
    public List<MemberDto> findMemberRanking(int page, int content) {
        return query.select(new QMemberDto(
                        m.id,
                        m.createDate,
                        m.modifyDate,
                        m.bronze,
                        m.silver,
                        m.gold,
                        m.diamond,
                        m.ruby,
                        m.platinum,
                        m.username,
                        m.nickname,
                        m.baekJoonName,
                        m.about,
                        m.profileImg,
                        m.provider,
                        m.email,
                        m.token,
                        m.newMember,
                        m.lastSolvedProblemId,
                        m.ranking
                )).from(m)
                .orderBy(nullsLast(m.ranking), m.ranking.asc())
                .offset(page * content)
                .limit(content)
                .fetch();
    }

    //-- 문제 해결순으로 정렬한 모든 member 목록 --//
    public List<Member> findMemberRanking() {
        return query
                .selectFrom(m)
                .orderBy(Expressions.numberTemplate(
                        Integer.class,
                        "{0} + {1} + {2} + {3} + {4} + {5}",
                        m.bronze, m.silver, m.gold, m.platinum, m.ruby, m.diamond
                ).desc())
                .where(m.baekJoonName.isNotNull())
                .fetch();
    }

    private <T extends Comparable> OrderSpecifier<T> nullsLast(Path<T> path) {
        return new OrderSpecifier<>(Order.ASC, path, OrderSpecifier.NullHandling.NullsLast);
    }

    //-- find by input --//
    public List<MemberDto> findByInput(String input, int page, int content) {
        return query.select(new QMemberDto(
                        m.id,
                        m.createDate,
                        m.modifyDate,
                        m.bronze,
                        m.silver,
                        m.gold,
                        m.diamond,
                        m.ruby,
                        m.platinum,
                        m.username,
                        m.nickname,
                        m.baekJoonName,
                        m.about,
                        m.profileImg,
                        m.provider,
                        m.email,
                        m.token,
                        m.newMember,
                        m.lastSolvedProblemId,
                        m.ranking
                )).from(m)
                .where(m.nickname.like("%" + input + "%"))
                .offset(page * content)
                .limit(content)
                .fetch();
    }
}

