package com.baeker.member.member.application.port.in;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.reqDto.MyStudyReqDto;

public interface MemberDeleteUseCase {
    Member deleteMyStudy(MyStudyReqDto dto);
}
