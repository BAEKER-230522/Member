package com.baeker.member.member.application.service;

import com.baeker.member.member.application.port.in.MemberQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberQueryService implements MemberQueryUseCase {

}
