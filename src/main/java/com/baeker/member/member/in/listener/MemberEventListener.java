package com.baeker.member.member.in.listener;

import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberService memberService;


    //-- when create my study --//
    @EventListener
    public void listen(CreateMyStudyEvent event) {
        memberService.createMyStudy(event);
    }

    //-- when update all member's solved count --//
    @EventListener
    public void listen(AddSolvedCountEvent event) {
        memberService.addSolvedCount(event);
    }

    //-- when con baekjoon --//
    @EventListener
    public void listen(ConBjEvent event) {
        memberService.conBj(event);
    }

}
