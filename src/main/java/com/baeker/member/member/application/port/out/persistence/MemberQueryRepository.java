package com.baeker.member.member.application.port.out.persistence;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;

import java.util.List;

public interface MemberQueryRepository {
    List<SchedulerResDto> findAllConBJ();
    List<MemberDto> findByMemberList(List<Long> memberIds, String status);
    List<Member> findMemberRanking();
    List<MemberDto> findMemberRanking(int page, int content);
    List<MemberDto> findByInput(String input, int page, int content);
}

