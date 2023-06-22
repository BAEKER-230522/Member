package com.baeker.member.member.in.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddSolvedCountEvent extends ApplicationEvent {

    private Long id;
    int bronze;
    int silver;
    int gold;
    int diamond;
    int ruby;
    int platinum;

    public AddSolvedCountEvent(Object source, Long id, int bronze, int silver, int gold, int diamond, int ruby, int platinum) {
        super(source);
        this.id = id;
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.diamond = diamond;
        this.ruby = ruby;
        this.platinum = platinum;
    }

}
