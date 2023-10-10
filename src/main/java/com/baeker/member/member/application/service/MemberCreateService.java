package com.baeker.member.member.application.service;

import com.baeker.member.base.error.exception.InvalidDuplicateException;
import com.baeker.member.base.error.exception.NotFoundException;
import com.baeker.member.member.application.port.in.MemberCreateUseCase;
import com.baeker.member.member.application.port.in.MemberQueryUseCase;
import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCreateService implements MemberCreateUseCase {
    private final MemberRepositoryPort memberRepositoryPort;
    private final MemberQueryUseCase memberQueryUseCase;

    @Override
    public Member create(JoinReqDto dto) {

        try {
            memberQueryUseCase.findByUsername(dto.getUsername());
            throw new InvalidDuplicateException("이미 존재하는 username 입니다.");
        } catch (NotFoundException e) {
            Member member = Member.createMember(dto);
            return memberRepositoryPort.save(member);
        }
    }

    @Override
    public Member whenSocialLogin(String providerType, String username, String email, String profileImg) {

        Member member = null;
        try {
            member = memberQueryUseCase.findByUsername(username);
        } catch (NotFoundException e) {
            return socialJoin(providerType, username, "", email, profileImg); // 최초 1회 실행
        }
        return member;
    }

    @Override
    public Member socialJoin(String providerType, String username, String password, String email, String profileImg) {
        JoinReqDto dto = new JoinReqDto();
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setProvider(providerType);
        dto.setProfileImage(profileImg);
        dto.setNickName(username);

        Member member = create(dto);
        memberRepositoryPort.save(member);
        return member;
    }
    @Override
    public void createMyStudy(CreateMyStudyEvent event) {
        Member member = memberQueryUseCase.findById(event.getMemberId());
        member.addMyStudy(event.getMyStudyId());
    }
}
