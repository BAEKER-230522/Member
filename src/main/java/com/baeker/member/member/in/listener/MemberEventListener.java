package com.baeker.member.member.in.listener;

import com.baeker.member.member.domain.service.MemberService;
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

    private final MemberService memberService;


    //-- when create my study --//
    @EventListener
    public void listen(CreateMyStudyEvent event) {
        log.info("study 생성 event 확인 member id = {} / my study id = {}", event.getMemberId(), event.getMyStudyId());
        memberService.createMyStudy(event);
    }

    //-- when update all member's solved count --//
    @EventListener
    public void listen(AddSolvedCountEvent event) {
        log.info("solved count update event 확인 member id = {}", event.getId());
        memberService.addSolvedCount(event);
    }

    //-- when con baekjoon --//
    @EventListener
    public void listen(ConBjEvent event) {
        log.info("백준 연동 event 확인 member id = {} , baekjoon id = {}", event.getId(), event.getBaekJoonName());
        memberService.conBj(event);
    }

}
