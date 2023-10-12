package com.baeker.member.member.adapter.in;

import com.baeker.member.base.request.RsData;
import com.baeker.member.member.application.port.in.MemberCreateUseCase;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${custom.mapping.member.web}")
@RequiredArgsConstructor
public class MemberCreateController {
    private final MemberCreateUseCase memberCreateUseCase;

    @Operation(summary = "member 생성")
    @PostMapping("/v1/create")
    public RsData<MemberDto> create(@RequestBody @Valid JoinReqDto dto) {
        Member member = memberCreateUseCase.create(dto);
        MemberDto createMemberDto = new MemberDto(member);
        return RsData.successOf(createMemberDto);
    }
}
