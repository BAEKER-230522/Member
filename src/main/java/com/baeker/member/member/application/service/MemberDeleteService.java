package com.baeker.member.member.application.service;

import com.baeker.member.base.error.exception.NotFoundException;
import com.baeker.member.member.application.port.in.MemberDeleteUseCase;
import com.baeker.member.member.application.port.in.MemberQueryUseCase;
import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.reqDto.MyStudyReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberDeleteService implements MemberDeleteUseCase {
    private final MemberRepositoryPort memberRepositoryPort;
    private final MemberQueryUseCase memberQueryUseCase;

    public Member deleteMyStudy(MyStudyReqDto dto) {
        Member member = memberQueryUseCase.findById(dto.getMemberId());

        if (!member.getMyStudies().contains(dto.getMyStudyId()))
            throw new NotFoundException("존재하지 않는 my study id / id = " + dto.getMyStudyId());

        member.getMyStudies().remove(dto.getMyStudyId());
        return member;
    }
}
