package com.baeker.member.member.in.listener;

import com.baeker.member.member.domain.service.MemberService;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
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

    @EventListener
    public void listen(AddSolvedCountEvent event) {
        memberService.addSolveCount(event);
    }

    @EventListener
    public void listen(CreateMyStudyEvent event) {
        memberService.createMyStudy(event);
    }
}
