package com.baeker.member.member.domain.service;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.BaekJoonDto;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.out.SnapshotRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
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
    @DisplayName("My Study 추가하는 이벤트 검증")
    void no01() {
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
    @DisplayName("백준 연동 그리고 스냅샷 생성 검증")
    void no02() {
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
    @DisplayName("Solved count 최신화와 Snapshot 업데이트")
    void no03() {
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
    @DisplayName("snapshot 날짜별 저장과 기록 삭제")
    void no04() {
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

    @Test
    @DisplayName("ranking update")
    void no05() {
        Member member1 = createMember("user1", "member1", "bk1", 5);
        Member member2 = createMember("user2", "member2", "bk2", 8);
        Member member3 = createMember("user3", "member3", "bk3", 2);
        Member member4 = createMember("user4", "member4", "bk4", 7);
        Member member5 = createMember();

        List<Member> all = memberService.finAll();
        assertThat(all.size()).isEqualTo(5);

        memberService.updateRanking();

        assertThat(member1.getRanking()).isEqualTo(3);
        assertThat(member2.getRanking()).isEqualTo(1);
        assertThat(member3.getRanking()).isEqualTo(4);
        assertThat(member4.getRanking()).isEqualTo(2);
        assertThat(member5.getRanking()).isNull();

        List<MemberDto> page1 = memberService.findMemberRanking(0, 2);
        assertThat(page1.get(0).getNickname()).isEqualTo("member2");

        List<MemberDto> page2 = memberService.findMemberRanking(1, 2);
        assertThat(page2.get(0).getNickname()).isEqualTo("member1");

        List<MemberDto> page3 = memberService.findMemberRanking(2, 2);
        assertThat(page3.get(0).getNickname()).isEqualTo("member");
    }

    private Member createMember() {
        JoinReqDto reqDto = JoinReqDto.createJoinDto("user", "member", "1234", "BAEKER", "aaa@aa.com", "123", "img");
        Member createMember = memberService.create(reqDto);
        return createMember;
    }

    private Member createMember(String user, String nickname, String baekjoon, int solved) {
        JoinReqDto reqDto = JoinReqDto.createJoinDto(user, nickname, "1234", "BAEKER", "aaa@aa.com", "123", "img");
        Member createMember = memberService.create(reqDto);

        publisher.publishEvent(new ConBjEvent(this, createMember.getId(), baekjoon, solved, 0,0,0,0,0));
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