package com.baeker.member.member.application.port.in;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.JoinReqDto;

public interface MemberCreateUseCase {
    Member create(JoinReqDto dto);
    Member socialJoin(String providerType, String username, String password, String email, String profileImg);
    Member whenSocialLogin(String providerType, String username, String email, String profileImg);
    void createMyStudy(CreateMyStudyEvent event);
}
