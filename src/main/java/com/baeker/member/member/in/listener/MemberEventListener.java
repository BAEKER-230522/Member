package com.baeker.member.member.in.listener;

import com.baeker.member.member.application.port.in.MemberModifyUseCase;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberModifyUseCase memberModifyUseCase;


    //-- when create my study --//
    @EventListener
    public void listen(CreateMyStudyEvent event) {
        memberModifyUseCase.createMyStudy(event);
    }

    //-- when update all member's solved count --//
    @EventListener
    public void listen(AddSolvedCountEvent event) {
        memberModifyUseCase.addSolvedCount(event);
    }

    //-- when con baekjoon --//
    @EventListener
    public void listen(ConBjEvent event) {
        memberModifyUseCase.conBj(event);
    }
}
