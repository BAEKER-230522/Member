package com.baeker.member.member.domain.entity;

import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.reqDto.BaekJoonDto;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
public class MemberSnapshot extends BaseEntity{


    private String dayOfWeek;

    @ManyToOne(fetch = LAZY)
    private Member member;


    //-- create score --//
    public static MemberSnapshot create(Member member, BaekJoonDto dto, String dayOfWeek) {
        MemberSnapshot snapshot = MemberSnapshot.builder()
                .member(member)
                .baekJoonName(member.getBaekJoonName())
                .dayOfWeek(dayOfWeek)
                .bronze(dto.getBronze())
                .silver(dto.getSilver())
                .gold(dto.getGold())
                .diamond(dto.getDiamond())
                .ruby(dto.getRuby())
                .platinum(dto.getPlatinum())
                .build();

        member.getSnapshotList().add(0, snapshot);
        return snapshot;
    }

    //-- update score --//
    public MemberSnapshot update(BaekJoonDto dto) {
        return this.toBuilder()
                .bronze(this.bronze + dto.getBronze())
                .silver(this.silver + dto.getSilver())
                .gold(this.gold + dto.getGold())
                .diamond(this.diamond + dto.getDiamond())
                .ruby(this.ruby + dto.getRuby())
                .platinum(this.platinum + dto.getPlatinum())
                .build();
    }
}
