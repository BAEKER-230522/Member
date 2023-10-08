package com.baeker.member.member.application.service;

import com.baeker.member.member.application.port.in.MemberCreateUseCase;
import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCreateService implements MemberCreateUseCase {
    private final MemberRepositoryPort memberRepositoryPort;


}
