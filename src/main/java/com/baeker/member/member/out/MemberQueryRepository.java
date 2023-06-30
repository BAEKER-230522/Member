package com.baeker.member.member.out;

import com.baeker.member.member.domain.entity.QMember;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.QMemberDto;
import com.baeker.member.member.in.resDto.QSchedulerResDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
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
    public List<MemberDto> findByMemberList(List<Long> memberIds) {
        return query
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
                        m.kakaoProfileImage,
                        m.provider,
                        m.email,
                        m.token,
                        m.newMember
                ))
                .from(m)
                .where(m.id.in(memberIds))
                .fetch();
    }
}

