package com.baeker.member.member.in.reqDto;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaekJoonDto {

    private Long id;
    private String baekJoonName;

    int bronze;
    int silver;
    int gold;
    int diamond;
    int ruby;
    int platinum;

    public BaekJoonDto(ConBjEvent event) {
        this.id = event.getId();
        this.baekJoonName = event.getBaekJoonName();
        this.bronze = event.getBronze();
        this.silver = event.getSilver();
        this.gold = event.getGold();
        this.diamond = event.getDiamond();
        this.ruby = event.getRuby();
        this.platinum = event.getPlatinum();
    }

    public BaekJoonDto(AddSolvedCountEvent event) {
        this.id = event.getId();
        this.bronze = event.getBronze();
        this.silver = event.getSilver();
        this.gold = event.getGold();
        this.diamond = event.getDiamond();
        this.ruby = event.getRuby();
        this.platinum = event.getPlatinum();
    }

    public BaekJoonDto(SolvedCountReqDto dto) {
        this.id = dto.getId();
        this.bronze = dto.getBronze();
        this.silver = dto.getSilver();
        this.gold = dto.getGold();
        this.diamond = dto.getDiamond();
        this.ruby = dto.getRuby();
        this.platinum = dto.getPlatinum();
    }

    public BaekJoonDto(Member member) {
        this.id = member.getId();
        this.baekJoonName = member.getBaekJoonName();
        this.bronze = member.getBronze();
        this.silver = member.getSilver();
        this.gold = member.getGold();
        this.diamond = member.getDiamond();
        this.ruby = member.getRuby();
        this.platinum = member.getPlatinum();
    }
}
