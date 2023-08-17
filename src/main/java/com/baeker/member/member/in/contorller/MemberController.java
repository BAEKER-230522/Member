package com.baeker.member.member.in.contorller;

import com.baeker.member.base.request.RsData;
import com.baeker.member.base.security.jwt.JwtService;
import com.baeker.member.member.in.reqDto.*;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import com.baeker.member.member.in.resDto.LastSolvedDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.UpdateResDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    //-- create --//
    @Operation(summary = "member 생성")
    @PostMapping("/v1/create")
    public RsData<MemberDto> create(@RequestBody @Valid JoinReqDto dto) {
        log.info("member 생성 요청 확인 username = {}", dto.getUsername());

        Member member = memberService.create(dto);
        MemberDto createMemberDto = new MemberDto(member);

        log.info("member 생성 완료 member id = {}", member.getId());
        return RsData.successOf(createMemberDto);
    }

    //-- 백준 id 연동 --//
    @Operation(summary = "백준 id 와 연동하기")
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
    @Operation(summary = "닉네임, 자기 소개, 프로필 사진 업데이트")
    @PostMapping("/v1/update")
    public RsData<UpdateResDto> update(@RequestBody @Valid UpdateReqDto dto) {
        log.info("member update 요청 확인 id = {}", dto.getId());

        Member member = memberService.update(dto);
        UpdateResDto response = new UpdateResDto(member);

        log.info("member update 완료");
        return RsData.successOf(response);
    }

    //-- update my study --//
    @Operation(summary = "My study 추가")
    @PostMapping("/v1/my-study")
    public RsData myStudyUpdate(@RequestBody @Valid MyStudyReqDto dto) {
        log.info("my study 등록 요청 확인 member = {} / my study = {}", dto.getMemberId(), dto.getMyStudyId());

        Member member = memberService.updateMyStudy(dto);

        log.info("my study 등록 완료");
        return RsData.of("S-1", "my study size - " + member.getMyStudies().size());
    }

    //-- update profile img --//
    @Operation(summary = "프로필 이미지 업데이트")
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
    @Operation(summary = "My Study 제거")
    @DeleteMapping("/v1/my-study")
    public RsData deleteMyStudy(@RequestBody @Valid MyStudyReqDto dto) {
        log.info("my study 삭제 요청 확인 member = {} / my study = {}", dto.getMemberId(), dto.getMyStudyId());

        Member member = memberService.deleteMyStudy(dto);

        log.info("my study 등록 완료");
        return RsData.of("S-1", "my study size - " + member.getMyStudies().size());
    }

    //-- update last solved --//
    @Operation(summary = "마지막으로 해결한 문제 id")
    @PostMapping("/v1/last-solved")
    public RsData<LastSolvedDto> updateLastSolved(
            @RequestBody @Valid UpdateLastSolvedReqDto dto
    ) {
        log.info("마지막 제출 문제 업데이트 요청 확인 member id = {} / 문제 번호 = {}", dto.getMemberId(), dto.getProblemId());

        LastSolvedDto resDto = new LastSolvedDto(
                memberService.updateLastSolved(dto)
        );

        log.info("마지막 제출 문제 업데이트 완료 member id = {} / 문제 번호 = {}", dto.getMemberId(), dto.getProblemId());
        return RsData.successOf(resDto);
    }

    @Operation(summary = "member solved count 최신화")
    @PostMapping("/solved")
    public RsData updateSolved(
            @RequestBody @Valid SolvedCountReqDto dto
    ) {
        log.info("solved count 최신화 요청 확인 member id = {}", dto.getId());
        Member member = memberService.addSolvedCount(dto);
        log.info("update 성공 member id = {} / solved count = {}", dto.getId(), member.solvedCount());
        return RsData.of("S-1", "성공", "총 " + member.solvedCount() + "문제 해결");
    }

    /**
     * access token 만료된 경우
     * token 재발급
     */
    @PostMapping("/v1/accessToken/expired")
    public RsData<JwtTokenResponse> accessTokenExpired(
            @RequestHeader("refreshToken") @Valid String refreshToken
    ) throws IOException {
        JwtTokenResponse newAccessToken = jwtService.createNewAccessToken(refreshToken);
        return RsData.successOf(newAccessToken);
    }
}
