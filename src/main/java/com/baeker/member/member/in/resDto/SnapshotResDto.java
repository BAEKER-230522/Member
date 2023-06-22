package com.baeker.member.member.in.resDto;

import com.baeker.member.member.domain.entity.MemberSnapshot;
import lombok.Data;

@Data
public class SnapshotResDto {
    private Long id;
    private String dayOfWeek;
    private int bronze;
    private int silver;
    private int gold;
    private int diamond;
    private int ruby;
    private int platinum;
    private int solvedCount;

    public SnapshotResDto(MemberSnapshot snapshot) {
        this.id = snapshot.getId();
        this.dayOfWeek = snapshot.getDayOfWeek();
        this.bronze = snapshot.getBronze();
        this.silver = snapshot.getSilver();
        this.gold = snapshot.getGold();
        this.diamond = snapshot.getDiamond();
        this.ruby = snapshot.getRuby();
        this.platinum = snapshot.getPlatinum();
        this.solvedCount = snapshot.solvedCount();
    }
}
