package com.baeker.member.member.domain.entity;

import com.baeker.member.member.in.event.ConBjEvent;
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
    public static MemberSnapshot create(Member member, ConBjEvent event, String dayOfWeek) {
        MemberSnapshot snapshot = MemberSnapshot.builder()
                .member(member)
                .baekJoonName(member.getBaekJoonName())
                .dayOfWeek(dayOfWeek)
                .bronze(event.getBronze())
                .sliver(event.getSliver())
                .gold(event.getGold())
                .diamond(event.getDiamond())
                .ruby(event.getRuby())
                .platinum(event.getPlatinum())
                .build();

        member.getSnapshotList().add(0, snapshot);
        return snapshot;
    }
}
