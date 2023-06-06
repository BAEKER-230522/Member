package com.baeker.member.member.in.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateMyStudyEvent extends ApplicationEvent {

    private Long memberId;
    private Long myStudyId;

    public CreateMyStudyEvent(Object source, Long memberId, Long myStudyId) {
        super(source);
        this.memberId = memberId;
        this.myStudyId = myStudyId;
    }
}
