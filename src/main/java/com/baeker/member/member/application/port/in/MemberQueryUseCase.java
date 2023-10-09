package com.baeker.member.member.application.port.in;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.reqDto.PageReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MemberQueryUseCase {
    Member findByUsername(String username);
    List<Member> finAll();
    Page<Member> getAll(PageReqDto dto);
    List<SchedulerResDto> findAllConBJ();

    Member findById(Long memberId);
    List<MemberDto> findByMyStudyList(List<Long> memberIds, String status);

    Member findByBaekJoonName(String baekJoonName);
    List<MemberSnapshot> findAllSnapshot(Member member);
    MemberSnapshot findTodaySnapshot(Member member);
    List<MemberDto> findMemberRanking(int page, int content);
}
