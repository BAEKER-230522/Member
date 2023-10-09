package com.baeker.member.member.application.service;

import com.baeker.member.base.error.exception.NotFoundException;
import com.baeker.member.member.application.port.in.MemberQueryUseCase;
import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.reqDto.PageReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberQueryService implements MemberQueryUseCase {
    private final MemberRepositoryPort memberRepositoryPort;

    @Override
    public Member findByUsername(String username) {
        Optional<Member> byUsername = memberRepositoryPort.findByUsername(username);
        if (byUsername.isPresent())
            return byUsername.get();
        throw new NotFoundException("존재하지 않는 username");
    }

    @Override
    public List<Member> finAll() {
        return memberRepositoryPort.findAll();
    }

    @Override
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
        return memberRepositoryPort.findAll(pageable);
    }

    @Override
    public List<SchedulerResDto> findAllConBJ() {
        List<SchedulerResDto> memberList = memberRepositoryPort.findAllConBJ();

        if (memberList.size() == 0)
            throw new NotFoundException("백준 연동된 회원이 없습니다.");

        return memberList;
    }

    @Override
    public Member findById(Long memberId) {
        Optional<Member> byId = memberRepositoryPort.findById(memberId);

        if (byId.isPresent())
            return byId.get();

        throw new NotFoundException("존재하지 않는 id / id = " + memberId);
    }

    @Override
    public List<MemberDto> findByMyStudyList(List<Long> memberIds, String status) {
        return memberRepositoryPort.findByMemberList(memberIds, status);
    }


    @Override
    public Member findByBaekJoonName(String baekJoonName) {
        Optional<Member> byBaekJoonName = memberRepositoryPort.findByBaekJoonName(baekJoonName);

        if (byBaekJoonName.isPresent())
            return byBaekJoonName.get();

        throw new NotFoundException("존재하지 않는 백준 name / name = " + baekJoonName);
    }



    //-- find all snapshot / 삭제 예정 --//
    @Override
    public List<MemberSnapshot> findAllSnapshot(Member member) {
        return snapshotQueryRepository.findByMemberId(member);
    }

    //-- find today snapshot --//
    @Override
    public MemberSnapshot findTodaySnapshot(Member member) {
        List<MemberSnapshot> snapshots = member.getSnapshotList();

        if (snapshots.size() == 0)
            throw new NotFoundException("백준 연동이 되어있지 않은 회원 입니다.");

        return member.getSnapshotList().get(snapshots.size() - 1);
    }

    //-- find member ranking --//
    @Override
    public List<MemberDto> findMemberRanking(int page, int content) {
        return memberRepositoryPort.findMemberRanking(page, content);
    }
}
