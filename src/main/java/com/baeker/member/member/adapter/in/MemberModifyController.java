package com.baeker.member.member.adapter.in;

import com.baeker.member.base.request.RsData;
import com.baeker.member.global.security.jwt.JwtService;
import com.baeker.member.member.application.port.in.MemberModifyUseCase;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.reqDto.MyStudyReqDto;
import com.baeker.member.member.in.reqDto.SolvedCountReqDto;
import com.baeker.member.member.in.reqDto.UpdateLastSolvedReqDto;
import com.baeker.member.member.in.reqDto.UpdateReqDto;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import com.baeker.member.member.in.resDto.LastSolvedDto;
import com.baeker.member.member.in.resDto.UpdateResDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${custom.mapping.member.web}")
public class MemberModifyController {
    private final MemberModifyUseCase memberModifyUseCase;
    private final JwtService jwtService;

    @Operation(summary = "백준 id 와 연동하기")
    @PostMapping("/v1/connect/baekjoon")
    public RsData connect(
            @RequestHeader("Authorization") String token,
            @RequestParam @Valid String name
    ) {
        Long memberId = Long.parseLong(jwtService.getClaimValue(token, "id"));
        Member member = memberModifyUseCase.connectBaekjoon(memberId, name);
        return RsData.successOf(member.getId());
    }

    //-- update 닉네임, 소개,프로필 사진 --//
    @Operation(summary = "닉네임, 자기 소개, 프로필 사진 업데이트")
    @PostMapping("/v1/update")
    public RsData<UpdateResDto> update(
            @RequestHeader("Authorization") String token,
            @RequestPart @Valid UpdateReqDto dto,
            @RequestPart MultipartFile img
    ) {
        Member member = memberModifyUseCase.update(dto, img);
        UpdateResDto response = new UpdateResDto(member);
        return RsData.successOf(response);
    }

    @Operation(summary = "닉네임, 자기 소개 업데이트")
    @PostMapping("/v1/profile")
    public RsData<UpdateResDto> updateProfile(
            @RequestBody @Valid UpdateReqDto dto
    ) {
        Member member = memberModifyUseCase.updateProfile(dto);
        UpdateResDto response = new UpdateResDto(member);
        return RsData.successOf(response);
    }

    //-- update my study --//
    @Operation(summary = "My study 추가")
    @PostMapping("/v1/my-study")
    public RsData myStudyUpdate(@RequestBody @Valid MyStudyReqDto dto) {
        Member member = memberModifyUseCase.updateMyStudy(dto);
        return RsData.of("S-1", "my study size - " + member.getMyStudies().size());
    }

    //-- update profile img --//
    @Operation(summary = "프로필 이미지 업데이트")
    @PostMapping("/v1/profile-img")
    public RsData updateProfileImg(
            @RequestHeader("Authorization") String token,
            @RequestPart MultipartFile img
    ) {
        Long id = Long.parseLong(jwtService.getClaimValue(token, "id"));
        Member member = memberModifyUseCase.updateImg(img, id);
        return RsData.of("S-1", " 이미지 update 완료", member.getProfileImg());
    }

    @Operation(summary = "마지막으로 해결한 문제 id")
    @PostMapping("/v1/last-solved")
    public RsData<LastSolvedDto> updateLastSolved(
            @RequestBody @Valid UpdateLastSolvedReqDto dto
    ) {
        LastSolvedDto resDto = new LastSolvedDto(
                memberModifyUseCase.updateLastSolved(dto)
        );
        return RsData.successOf(resDto);
    }

    @Operation(summary = "member solved count 최신화")
    @PostMapping("/solved")
    public RsData updateSolved(
            @RequestBody @Valid SolvedCountReqDto dto
    ) {
        Member member = memberModifyUseCase.addSolvedCount(dto);
        return RsData.of("S-1", "성공", "총 " + member.solvedCount() + "문제 해결");
    }

    @Operation(summary = "Member 랭킹 수동 업데이트 / admin 만 할 수 있도록 수정 예정")
    @PostMapping("/v1/ranking")
    public RsData updateRanking() {
        memberModifyUseCase.updateRanking();
        return RsData.of("S-1", "성공");
    }


    @PostMapping("/v1/accessToken/expired")
    public RsData<JwtTokenResponse> accessTokenExpired(
            @RequestHeader("refreshToken") @Valid String refreshToken
    ) throws IOException {
        JwtTokenResponse newAccessToken = jwtService.createNewAccessToken(refreshToken);
        return RsData.successOf(newAccessToken);
    }
}
