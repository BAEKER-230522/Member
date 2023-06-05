package com.baeker.member.member.domain.service;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ApplicationEventPublisher publisher;


    @Test
    void My_Study_추가_이벤트() {
        Member createMember = createMember();

        Member findMember = memberService.findById(createMember.getId());

        assertThat(findMember.getNickname()).isEqualTo("member");
        assertThat(findMember.getMyStudies().size()).isEqualTo(0);

        CreateMyStudyEvent event = new CreateMyStudyEvent(this, findMember.getId(), 1L);
        publisher.publishEvent(event);

        assertThat(findMember.getMyStudies().size()).isEqualTo(1);

        CreateMyStudyEvent event2 = new CreateMyStudyEvent(this, findMember.getId(), 2L);
        publisher.publishEvent(event);

        Member member = memberService.findById(createMember.getId());
        assertThat(member.getMyStudies().size()).isEqualTo(2);
    }

    @Test
    void 백준_연동과_snapshot_생성() {
        Member createMember = createMember();

        Member findMember = memberService.findById(createMember.getId());
        assertThat(findMember.solvedCount()).isEqualTo(0);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(0);

        ConBjEvent event = new ConBjEvent(
                this, findMember.getId(), "baek",
                1, 1, 1, 1, 1, 1
        );
        publisher.publishEvent(event);
        List<MemberSnapshot> allSnapshot = memberService.findAllSnapshot();

        assertThat(findMember.solvedCount()).isEqualTo(6);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(1);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(allSnapshot.size());
    }

    @Test
    void SolvedCount_최신화와_snapshot_생성_삭제() {

    }


    private Member createMember() {
        JoinReqDto reqDto = JoinReqDto.createJoinDto("user", "member", "1234", "BAEKER", "aaa@aa.com", "123", "img");
        Member createMember = memberService.create(reqDto);
        return createMember;
    }
}