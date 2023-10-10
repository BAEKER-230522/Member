package com.baeker.member.member.adapter.in;

import com.baeker.member.base.request.RsData;
import com.baeker.member.member.application.port.in.MemberDeleteUseCase;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.reqDto.MyStudyReqDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${custom.mapping.member.web}")
public class MemberDeleteController {
    private final MemberDeleteUseCase memberDeleteUseCase;

    //-- delete my study --//
    @Operation(summary = "My Study 제거")
    @DeleteMapping("/v1/my-study")
    public RsData deleteMyStudy(@RequestBody @Valid MyStudyReqDto dto) {
        Member member = memberDeleteUseCase.deleteMyStudy(dto);
        return RsData.of("S-1", "my study size - " + member.getMyStudies().size());
    }
}
