package com.baeker.member.member.in.contorller;

import com.baeker.member.base.request.RsData;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.reqDto.PageReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/member/get")
@RequiredArgsConstructor
public class MemberFindController {

    private final MemberService memberService;

    //-- find by username --//
    @GetMapping("/v1/username")
    public RsData findByUsername(@RequestParam @Valid String username) {
        log.info("find by username 요청 확인 /  username = {}", username);
        Member member = memberService.findByUsername(username);

        MemberDto dto = new MemberDto(member);

        log.info("member 응답 완료");
        return RsData.successOf(dto);
    }

    //-- find by id --//
    @GetMapping("/v1/id")
    public RsData findById(@RequestParam @Valid Long id) {
        log.info("find by id 요청 확인 / id = {}", id);
        Member member = memberService.findById(id);

        MemberDto dto = new MemberDto(member);

        log.info("member 응답 완료");
        return RsData.successOf(dto);
    }


    //-- find all for scheduler--//
    @GetMapping("/v1/all")
    public RsData findAllForScheduler() {
        log.info("모든 Member 조회 요청 확인");

        List<SchedulerResDto> dtoList = memberService.findAllConBJ();

        log.info("응답 완료 count = {}", dtoList.size());
        return RsData.of("S-1", "count = " + dtoList.size(), dtoList);
    }

    //-- find all for paging --//
    @GetMapping("/v1/pageAll")
    public RsData findAll(@ModelAttribute @Valid PageReqDto dto) {
        log.info("find all 페이징 요청 확인 / page = {}", dto.getPage());

        List<MemberDto> dtoList = memberService.getAll(dto)
                .stream()
                .map(m -> new MemberDto(m))
                .toList();

        log.info("응답 완료 count = {} / page = {}", dtoList.size(), dto.getPage());
        return RsData.of("S-1", "count = " + dtoList.size(), dtoList);
    }
}
