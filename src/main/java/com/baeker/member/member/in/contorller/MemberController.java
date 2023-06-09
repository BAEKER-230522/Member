package com.baeker.member.member.in.contorller;

import com.baeker.member.base.request.RsData;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.reqDto.MyStudyReqDto;
import com.baeker.member.member.in.reqDto.UpdateReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.UpdateResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //-- create --//
    @PostMapping("/v1/create")
    public RsData<MemberDto> create(@RequestBody @Valid JoinReqDto dto) {
        log.info("member 생성 요청 확인 username = {}", dto.getUsername());

        Member member = memberService.create(dto);
        MemberDto createMemberDto = new MemberDto(member);

        log.info("member 생성 완료 member id = {}", member.getId());
        return RsData.successOf(createMemberDto);
    }

    //-- 백준 id 연동 --//
    @PostMapping("/v1/connect/baekjoon/{id}")
    public RsData connect(
            @PathVariable Long id,
            @RequestParam @Valid String name
    ) {
        log.info("member 백준 id 연동 요청 확인 id = {}, baekjoon name = {}", id, name);

        Member member = memberService.connectBaekjoon(id, name);

        log.info("member 백준 id 연동 성공");
        return RsData.successOf(member.getId());
    }

    //-- update 닉네임, 소개,프로필 사진 --//
    @PostMapping("/v1/update")
    public RsData<UpdateResDto> update(@RequestBody @Valid UpdateReqDto dto) {
        log.info("member update 요청 확인 id = {}", dto.getId());

        Member member = memberService.update(dto);
        UpdateResDto response = new UpdateResDto(member);

        log.info("member update 완료");
        return RsData.successOf(response);
    }

    //-- update my study --//
    @PostMapping("/v1/my-study")
    public RsData myStudyUpdate(@RequestBody @Valid MyStudyReqDto dto) {
        log.info("my study 등록 요청 확인 member = {} / my study = {}", dto.getMemberId(), dto.getMyStudyId());

        Member member = memberService.updateMyStudy(dto);

        log.info("my study 등록 완료");
        return RsData.of("S-1", "my study size - " + member.getMyStudies().size());
    }

    //-- update profile img --//
    @PostMapping("/v1/profile-img/{id}")
    public RsData updateProfileImg(
            @PathVariable Long id,
            @RequestPart MultipartFile img
    ) {
        log.info("profile img 수정 요청 확인");

        Member member = memberService.updateImg(img, id);

        log.info("profile img 수정 완료");
        return RsData.of("S-1", " 이미지 update 완료", member.getProfileImg());
    }

    //-- delete my study --//
    @DeleteMapping("/v1/my-study")
    public RsData deleteMyStudy(@RequestBody @Valid MyStudyReqDto dto) {
        log.info("my study 삭제 요청 확인 member = {} / my study = {}", dto.getMemberId(), dto.getMyStudyId());

        Member member = memberService.deleteMyStudy(dto);

        log.info("my study 등록 완료");
        return RsData.of("S-1", "my study size - " + member.getMyStudies().size());
    }
}
