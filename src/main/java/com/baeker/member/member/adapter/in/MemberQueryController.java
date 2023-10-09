package com.baeker.member.member.adapter.in;

import com.baeker.member.base.request.RsData;
import com.baeker.member.global.security.jwt.JwtService;
import com.baeker.member.member.application.port.in.MemberQueryUseCase;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.reqDto.MembersReqDto;
import com.baeker.member.member.in.reqDto.PageReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import com.baeker.member.member.in.resDto.SnapshotResDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${custom.mapping.member.web}")
public class MemberQueryController {
    private final MemberQueryUseCase memberQueryUseCase;
    private final JwtService jwtService;
    @GetMapping("/v1/username")
    public RsData<MemberDto> findByUsername(@RequestParam @Valid String username) {
        Member member = memberQueryUseCase.findByUsername(username);

        MemberDto dto = new MemberDto(member);
        return RsData.successOf(dto);
    }

    //-- find by id --//
    @GetMapping("/v1/id")
    public RsData<MemberDto> findById(@RequestParam @Valid Long id) {
        Member member = memberQueryUseCase.findById(id);

        MemberDto dto = new MemberDto(member);
        return RsData.successOf(dto);
    }


    //-- 백준 연동한 모든 회원 조회 --//
    @GetMapping("/v1/all")
    public RsData<List<SchedulerResDto>> findAllForScheduler() {
        List<SchedulerResDto> dtoList = memberQueryUseCase.findAllConBJ();
        return RsData.of("S-1", "count = " + dtoList.size(), dtoList);
    }

    //-- find all for paging --//
    @GetMapping("/v1/pageAll")
    public RsData<List<MemberDto>> findAll(@ModelAttribute @Valid PageReqDto dto) {
        List<MemberDto> dtoList = memberQueryUseCase.getAll(dto)
                .stream()
                .map(MemberDto::new)
                .toList();
        return RsData.of("S-1", "count = " + dtoList.size(), dtoList);
    }

    //-- find today snapshot --//
    @GetMapping("/v1/snapshot/today")
    public RsData<SnapshotResDto> findTodaySnapshot(@RequestParam @Valid Long id) {

        Member member = memberQueryUseCase.findById(id);
        MemberSnapshot snapshot = memberQueryUseCase.findTodaySnapshot(member);
        SnapshotResDto resDto = new SnapshotResDto(snapshot);
        return RsData.successOf(resDto);
    }

    //-- find All snapshot --//
    @GetMapping("/v1/snapshot/week")
    public RsData<List<SnapshotResDto>> findAllSnapshot(@RequestParam @Valid Long id) {

        List<SnapshotResDto> resDtoList = memberQueryUseCase.findById(id)
                .getSnapshotList()
                .stream()
                .map(SnapshotResDto::new)
                .toList();
        return RsData.of("S-1", "Dto Size = " + resDtoList.size(), resDtoList);
    }

    //-- find all --//
    @GetMapping("/v1/all-members")
    public RsData<List<MemberDto>> findAllMembers() {
        List<MemberDto> resDtoList = memberQueryUseCase.finAll()
                .stream()
                .map(MemberDto::new)
                .toList();
        return RsData.of("S-1", "Dto Size = " + resDtoList.size(), resDtoList);
    }

    //-- find by my study list --//
    @PostMapping("/v1/id/list")
    @Operation(summary = "server 간 통신에 사용되는 api 입니다.")
    public RsData<List<MemberDto>> findListByMyStudy(
            @RequestBody MembersReqDto dto
    ) {
        List<Long> myStudyList = dto.getMembers();
        List<MemberDto> resDtoList = memberQueryUseCase.findByMyStudyList(myStudyList, dto.getStatus());
        return RsData.successOf(resDtoList);
    }

    //-- find member ranking --//
    @GetMapping("/v1/ranking")
    @Operation(summary = "member 문제 해결순 내림차순 목록 / page = 페이지, content = 페이지당 data 숫자")
    public RsData<List<MemberDto>> findMemberRanking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int content
    ) {
        List<MemberDto> dtoList = memberQueryUseCase.findMemberRanking(page, content);
        return RsData.successOf(dtoList);
    }

    //-- con baek joon check --//
    @Hidden
    @GetMapping("/test/baekjoon")
    public ResponseEntity<Boolean> isConnectBaekJoon(
            @RequestParam Long memberId
    ) {
        return ResponseEntity.ok(true);
    }
}
