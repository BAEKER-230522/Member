package com.baeker.member.member.domain.service;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.application.port.out.persistence.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberSchedulingService {

    private final MemberQueryRepository memberQueryRepository;

    @Transactional
    @Scheduled(cron = "0 0 20 * * *")
    public void updateRanking() {
        log.info("member ranking update 시작");

        List<Member> memberList = memberQueryRepository.findMemberRanking();

        for (int i = 0; i < memberList.size(); i++)
            memberList.get(i).updateRanking(i + 1);

        log.info("모든 member ranking update 완료");
    }
}
