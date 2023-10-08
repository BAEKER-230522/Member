package com.baeker.member.member.application.port.out.persistence;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.QMemberDto;
import com.baeker.member.member.in.resDto.QSchedulerResDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.baeker.member.member.domain.entity.QMember.member;

@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    //-- 백준 연동한 모든 회원 조회 --//
    @Override
    public List<SchedulerResDto> findAllConBJ() {
        return jpaQueryFactory
                .select(new QSchedulerResDto(
                        member.id,
                        member.baekJoonName,
                        member.bronze,
                        member.silver,
                        member.gold,
                        member.diamond,
                        member.ruby,
                        member.platinum,
                        member.lastSolvedProblemId
                ))
                .from(member)
                .where(member.baekJoonName.isNotNull())
                .fetch();
    }

    //-- id list 로 member list 조회 --//
    @Override
    public List<MemberDto> findByMemberList(List<Long> memberIds, String status) {
        List<MemberDto> result = jpaQueryFactory
                .select(new QMemberDto(
                       member.id,
                       member.createDate,
                       member.modifyDate,
                       member.bronze,
                       member.silver,
                       member.gold,
                       member.diamond,
                       member.ruby,
                       member.platinum,
                       member.username,
                       member.nickname,
                       member.baekJoonName,
                       member.about,
                       member.profileImg,
                       member.provider,
                       member.email,
                       member.token,
                       member.newMember,
                       member.lastSolvedProblemId,
                       member.ranking
                ))
                .from(member)
                .where(member.id.in(memberIds))
                .fetch();

        for (MemberDto dto : result) dto.setStatus(status);
        return result;
    }

    //-- 문제 해결 순위로 리스트 조회 --//
    @Override
    public List<MemberDto> findMemberRanking(int page, int content) {
        return jpaQueryFactory.select(new QMemberDto(
                        member.id,
                        member.createDate,
                        member.modifyDate,
                        member.bronze,
                        member.silver,
                        member.gold,
                        member.diamond,
                        member.ruby,
                        member.platinum,
                        member.username,
                        member.nickname,
                        member.baekJoonName,
                        member.about,
                        member.profileImg,
                        member.provider,
                        member.email,
                        member.token,
                        member.newMember,
                        member.lastSolvedProblemId,
                        member.ranking
                )).from(member)
                .orderBy(nullsLast(member.ranking), member.ranking.asc())
                .offset((long) page * content)
                .limit(content)
                .fetch();
    }

    //-- 문제 해결순으로 정렬한 모든 member 목록 --//
    @Override
    public List<Member> findMemberRanking() {
        return jpaQueryFactory
                .selectFrom(member)
                .orderBy(Expressions.numberTemplate(
                        Integer.class,
                        "{0} + {1} + {2} + {3} + {4} + {5}",
                        member.bronze, member.silver, member.gold, member.platinum, member.ruby, member.diamond
                ).desc())
                .where(member.baekJoonName.isNotNull())
                .fetch();
    }

    private <T extends Comparable> OrderSpecifier<T> nullsLast(Path<T> path) {
        return new OrderSpecifier<>(Order.ASC, path, OrderSpecifier.NullHandling.NullsLast);
    }

    //-- find by input --//
    @Override
    public List<MemberDto> findByInput(String input, int page, int content) {
        return jpaQueryFactory.select(new QMemberDto(
                        member.id,
                        member.createDate,
                        member.modifyDate,
                        member.bronze,
                        member.silver,
                        member.gold,
                        member.diamond,
                        member.ruby,
                        member.platinum,
                        member.username,
                        member.nickname,
                        member.baekJoonName,
                        member.about,
                        member.profileImg,
                        member.provider,
                        member.email,
                        member.token,
                        member.newMember,
                        member.lastSolvedProblemId,
                        member.ranking
                )).from(member)
                .where(member.nickname.like("%" + input + "%"))
                .offset((long) page * content)
                .limit(content)
                .fetch();
    }
}
