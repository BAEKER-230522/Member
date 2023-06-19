package com.baeker.member.member.domain.service;

import com.baeker.member.base.exception.InvalidDuplicateException;
import com.baeker.member.base.exception.NotFoundException;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.*;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import com.baeker.member.member.in.resDto.SnapshotQueryRepository;
import com.baeker.member.member.out.MemberQueryRepository;
import com.baeker.member.member.out.MemberRepository;
import com.baeker.member.member.out.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final SnapshotRepository snapshotRepository;
    private final SnapshotQueryRepository snapshotQueryRepository;


    /**
     * * CREATE METHOD **
     * member 객체 생성
     */

    //-- create member --//
    @Transactional
    public Member create(JoinReqDto dto) {

        try {
            this.findByUsername(dto.getUsername());
            throw new InvalidDuplicateException("이미 존재하는 username 입니다.");
        } catch (NotFoundException e) {
        }

        Member member = Member.createMember(dto.getProvider(), dto.getUsername(), dto.getNickName(), "", dto.getPassword(), dto.getProfileImage(), dto.getEmail(), dto.getToken());
        return memberRepository.save(member);
    }


    /**
     * * READ METHOD **
     * find by username
     * find all
     * find all + paging
     * find by id
     * find by 백준 name
     * find all snapshot
     * find today snapshot
     */

    //-- find by username --//
    public Member findByUsername(String username) {
        Optional<Member> byUsername = memberRepository.findByUsername(username);

        if (byUsername.isPresent())
            return byUsername.get();

        throw new NotFoundException("존재하지 않는 username");
    }

    //-- find all --//
    public List<Member> finAll() {
        return memberRepository.findAll();
    }

    //-- find all + paging --//
    public Page<Member> getAll(PageReqDto dto) {
        ArrayList<Sort.Order> sorts = new ArrayList<>();

        if (dto.isAsc())
            sorts.add(Sort.Order.asc(dto.getStandard()));
        else
            sorts.add(Sort.Order.desc(dto.getStandard()));

        Pageable pageable = PageRequest.of(
                dto.getPage(),
                dto.getMaxContent(),
                Sort.by(sorts)
        );
        return memberRepository.findAll(pageable);
    }

    //-- find all 백준 연동한 사람만 --//
    public List<SchedulerResDto> findAllConBJ() {
        List<SchedulerResDto> memberList = memberQueryRepository.findAllConBJ();

        if (memberList.size() == 0)
            throw new NotFoundException("백준 연동된 회원이 없습니다.");

        return memberList;
    }

    //-- find by id --//
    public Member findById(Long id) {
        Optional<Member> byId = memberRepository.findById(id);

        if (byId.isPresent())
            return byId.get();

        throw new NotFoundException("존재하지 않는 id / id = " + id);
    }

    //-- find by 백준 name --//
    public Member findByBaekJoonName(String baekJoonName) {
        Optional<Member> byBaekJoonName = memberRepository.findByBaekJoonName(baekJoonName);

        if (byBaekJoonName.isPresent())
            return byBaekJoonName.get();

        throw new NotFoundException("존재하지 않는 백준 name / name = " + baekJoonName);
    }

    //-- find all snapshot --//
    public List<MemberSnapshot> findAllSnapshot(Member member) {
        return snapshotQueryRepository.findByMemberId(member);
    }

    //-- find today snapshot --//
    public MemberSnapshot findTodaySnapshot(Member member) {
        return member.getSnapshotList().get(0);
    }


    /**
     * * UPDATE METHOD **
     * nickname, about, profile img 수정
     * update my study
     * delete my study
     * event : 백준 연동
     * evnet : Solved Count Update
     * event : when create my study
     * update snapshot
     */

    //-- nickname, about, profile img 수정 --//
    @Transactional
    public Member update(UpdateReqDto dto) {

        Member member = this.findById(dto.getId())
                .update(
                        dto.getNickname(),
                        dto.getAbout(),
                        dto.getProfileImg()
                );

        return memberRepository.save(member);
    }

    //-- update my study --//
    @Transactional
    public Member updateMyStudy(MyStudyReqDto dto) {
        Member member = this.findById(dto.getMemberId());

        if (member.getMyStudies().contains(dto.getMyStudyId()))
            throw new InvalidDuplicateException("이미 등록된 my study / my study id = " + dto.getMyStudyId());

        return member.updateMyStudy(dto.getMyStudyId());
    }

    //-- delete my study --//
    @Transactional
    public Member deleteMyStudy(MyStudyReqDto dto) {
        Member member = this.findById(dto.getMemberId());

        if (!member.getMyStudies().contains(dto.getMyStudyId()))
            throw new NotFoundException("존재하지 않는 my study id / id = " + dto.getMyStudyId());

        member.getMyStudies().remove(dto.getMyStudyId());
        return member;
    }

    //-- event : 백준 연동 --//
    public String conBj(ConBjEvent event) {

        Member member = this.findById(event.getId());

        try {
            this.findByBaekJoonName(event.getBaekJoonName());
            throw new InvalidDuplicateException(event.getBaekJoonName() + "은 이미 연동된 백준 id 입니다.");
        } catch (NotFoundException e) {
        }

        BaekJoonDto dto = new BaekJoonDto(event);
        this.updateSnapshot(member, dto);

        Member updateMember = member.connectBaekJoon(event);
        return memberRepository.save(updateMember).getBaekJoonName();
    }

    //-- event : solved count update --//
    public void addSolvedCount(AddSolvedCountEvent event) {

        Member member = this.findById(event.getId());

        BaekJoonDto dto = new BaekJoonDto(event);
        this.updateSnapshot(member, dto);

        memberRepository.save(member.updateSolvedCount(event));
    }

    //-- event : when create my study --//
    public void createMyStudy(CreateMyStudyEvent event) {
        Member member = this.findById(event.getMemberId());
        member.addMyStudy(event.getMyStudyId());
    }

    // update snapshot //
    private void updateSnapshot(Member member, BaekJoonDto dto) {
        String today = LocalDateTime.now().getDayOfWeek().toString();
        List<MemberSnapshot> snapshots = member.getSnapshotList();

        if (snapshots.size() == 0 || !snapshots.get(0).getDayOfWeek().equals(today)) {
            MemberSnapshot snapshot = MemberSnapshot.create(member, dto, today);
            snapshotRepository.save(snapshot);

        } else {
            MemberSnapshot snapshot = snapshots.get(0).update(dto);
            snapshotRepository.save(snapshot);
        }

        if (snapshots.size() == 8) {
            MemberSnapshot snapshot = snapshots.get(7);
            snapshots.remove(snapshot);
            snapshotRepository.delete(snapshot);
        }
    }
}
