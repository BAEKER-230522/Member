package com.baeker.member.member.in.contorller;

import com.baeker.member.base.request.RsData;
import com.baeker.member.global.security.jwt.JwtService;
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

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

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

    //-- solved count 최신화 --//
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

    //-- ranking 수동 업데이트 --//
    @Operation(summary = "Member 랭킹 수동 업데이트 / admin 만 할 수 있도록 수정 예정")
    @PostMapping("/v1/ranking")
    public RsData updateRanking() {
        log.info("member ranking 수동 update 요청 확인");

        memberService.updateRanking();

        log.info("member ranking 수동 update 완료");
        return RsData.of("S-1", "성공");
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
        log.info(newAccessToken.accessToken() + "new access token 발급 완료");
        return RsData.successOf(newAccessToken);
    }
}
