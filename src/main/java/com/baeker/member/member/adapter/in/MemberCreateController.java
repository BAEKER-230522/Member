package com.baeker.member.member.adapter.in;

import com.baeker.member.member.application.port.in.MemberCreateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${custom.mapping.member.web}")
@RequiredArgsConstructor
public class MemberCreateController {
    private final MemberCreateUseCase memberCreateUseCase;
}
