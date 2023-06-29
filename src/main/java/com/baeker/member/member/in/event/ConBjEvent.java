package com.baeker.member.member.in.event;

import com.baeker.member.member.out.resDto.ConBaekjoonResDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConBjEvent extends ApplicationEvent {

    private Long id;
    private String baekJoonName;

    int bronze;
    int silver;
    int gold;
    int diamond;
    int ruby;
    int platinum;

    public ConBjEvent(Object source, Long memberId, String baekJoonName, int bronze, int silver, int gold, int diamond, int ruby, int platinum) {
        super(source);
        this.id = memberId;
        this.baekJoonName = baekJoonName;
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.diamond = diamond;
        this.ruby = ruby;
        this.platinum = platinum;
    }

    public ConBjEvent(Object source, Long memberId, String baekJoonName, ConBaekjoonResDto dto) {
        super(source);
        this.id = memberId;
        this.baekJoonName = baekJoonName;
        this.bronze = dto.getBronze();
        this.silver = dto.getSilver();
        this.gold = dto.getGold();
        this.diamond = dto.getDiamond();
        this.ruby = dto.getRuby();
        this.platinum = dto.getPlatinum();
    }
}
