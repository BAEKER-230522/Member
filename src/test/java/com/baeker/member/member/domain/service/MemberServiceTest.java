package com.baeker.member.member.domain.service;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.BaekJoonDto;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import com.baeker.member.member.out.SnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private SnapshotRepository snapshotRepository;


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
        List<MemberSnapshot> allSnapshot = memberService.findAllSnapshot(findMember);

        assertThat(findMember.solvedCount()).isEqualTo(6);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(1);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(allSnapshot.size());
    }

    @Test
    void SolvedCount_최신화와_snapshot_업데이트() {
        Member member = createMember();
        Member findMember = memberService.findById(member.getId());
        publisher.publishEvent(new ConBjEvent(this, member.getId(), "baek",1, 1, 1, 1, 1, 1));

        assertThat(findMember.solvedCount()).isEqualTo(6);

        AddSolvedCountEvent event = new AddSolvedCountEvent(
                this, member.getId(),
                1, 1, 1, 1, 1, 1
        );
        publisher.publishEvent(event);
        List<MemberSnapshot> allSnapshot = memberService.findAllSnapshot(findMember);

        assertThat(findMember.solvedCount()).isEqualTo(12);
        assertThat(allSnapshot.size()).isEqualTo(1);
    }

    @Test
    void Snapshot_날짜별_저장과_기록삭제() {
        Member member = createMember();
        Member findMember = memberService.findById(member.getId());
        publisher.publishEvent(new ConBjEvent(this, member.getId(), "baek",1, 1, 1, 1, 1, 1));

        // test 용 스냅샷 6일치 생산
        for (int i = 1; i < 7; i++)
            testSnapshot(findMember, i);

        List<MemberSnapshot> allSnapshot = memberService.findAllSnapshot(findMember);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(7);
        assertThat(allSnapshot.size()).isEqualTo(7);

        AddSolvedCountEvent event = new AddSolvedCountEvent(
                this, member.getId(),
                1, 1, 1, 1, 1, 1
        );
        publisher.publishEvent(event);

        List<MemberSnapshot> allSnapshot1 = memberService.findAllSnapshot(findMember);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(7);
        assertThat(allSnapshot1.size()).isEqualTo(7);

        publisher.publishEvent(new AddSolvedCountEvent(this, member.getId(),1, 1, 1, 1, 1, 1));

        List<MemberSnapshot> allSnapshot2 = memberService.findAllSnapshot(member);
        assertThat(findMember.getSnapshotList().size()).isEqualTo(7);
        assertThat(allSnapshot2.size()).isEqualTo(7);

        assertThat(findMember.getSnapshotList().get(0).getDayOfWeek()).isEqualTo(LocalDateTime.now().getDayOfWeek().toString());
    }

    private Member createMember() {
        JoinReqDto reqDto = JoinReqDto.createJoinDto("user", "member", "1234", "BAEKER", "aaa@aa.com", "123", "img");
        Member createMember = memberService.create(reqDto);
        return createMember;
    }

    // test 용 스냅샷 생성 //
    private void testSnapshot(Member member, int lastDay) {
        String day = LocalDateTime.now().minusDays(lastDay).getDayOfWeek().toString();
        BaekJoonDto dto = new BaekJoonDto(member.getId(), member.getBaekJoonName(), 1, 1, 1, 1, 1, 1);
        MemberSnapshot snapshot = MemberSnapshot.create(member, dto, day);
        snapshotRepository.save(snapshot);
    }
}