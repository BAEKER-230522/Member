package com.baeker.member.member.domain.service;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private ApplicationEventPublisher publisher;


    @Test
    void My_Study_추가_이벤트() {
        JoinReqDto reqDto = JoinReqDto.createJoinDto("user", "member", "1234", "BAEKER", "aaa@aa.com", "123", "img");
        Member createMember = memberService.create(reqDto);

        Member findMember = memberService.findById(createMember.getId());

        assertThat(findMember.getNickname()).isEqualTo("member");
        assertThat(findMember.solvedCount()).isEqualTo(0);
        assertThat(findMember.getMyStudies().size()).isEqualTo(0);

        CreateMyStudyEvent event = new CreateMyStudyEvent(this, findMember.getId(), 1L);
        publisher.publishEvent(event);

        assertThat(findMember.getMyStudies().size()).isEqualTo(1);

        CreateMyStudyEvent event2 = new CreateMyStudyEvent(this, findMember.getId(), 2L);
        publisher.publishEvent(event);

        Member member = memberService.findById(createMember.getId());
        assertThat(member.getMyStudies().size()).isEqualTo(2);
    }
}