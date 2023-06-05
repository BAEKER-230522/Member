package com.baeker.member.member.in.contorller;

import com.baeker.member.base.request.RsData;
import com.baeker.member.member.in.reqDto.ConBjReqDto;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.reqDto.UpdateReqDto;
import com.baeker.member.member.in.resDto.ConBjResDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.UpdateResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //-- create --//
    @PostMapping("/v1/create")
    public RsData create(@RequestBody @Valid JoinReqDto dto) {
        log.info("member 생성 요청 확인 username = {}", dto.getUsername());

        Member member = memberService.create(dto);
        MemberDto createMemberDto = new MemberDto(member);

        log.info("member 생성 완료 member id = {}", member.getId());
        return RsData.successOf(createMemberDto);
    }

    //-- update 닉네임, 소개,프로필 사진 --//
    @PostMapping("/v1/update")
    public RsData update(@RequestBody @Valid UpdateReqDto dto) {
        log.info("member update 요청 확인 id = {}", dto.getId());

        Member member = memberService.update(dto);
        UpdateResDto response = new UpdateResDto(member);

        log.info("member update 완료");
        return RsData.successOf(response);
    }

    //-- 백준 연동 --//
//    @PostMapping("/v1/connect")
//    public RsData conBk(@RequestBody @Valid ConBjReqDto dto) {
//        log.info("백준 id 연동 요청 확인 id = {} / 백준 name = {}", dto.getId(), dto.getBaekJoonName());
//
//        String baekJoonName = memberService.conBj(dto);
//        ConBjResDto response = new ConBjResDto(baekJoonName);
//
//        return RsData.successOf(response);
//    }
}
