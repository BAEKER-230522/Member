package com.baeker.member.member.application.service;

import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberCreateTest {
    @InjectMocks
    private MemberCreateService memberCreateService;
    @Mock
    private MemberRepositoryPort memberRepositoryPort;

}
