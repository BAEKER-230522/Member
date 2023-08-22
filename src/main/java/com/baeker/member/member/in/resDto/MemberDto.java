package com.baeker.member.member.in.resDto;

import com.baeker.member.member.domain.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    int bronze;
    int silver;
    int gold;
    int diamond;
    int ruby;
    int platinum;
    int solvedBaekJoon;
    private String username;
    private String nickname;
    private String baekJoonName;
    private String about;
    private String profileImg;
    private String provider;
    private String email;
    private String token;
    private boolean newMember;
    private String status;
    private int lastSolvedProblemId;
    private Integer ranking;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.bronze = member.getBronze();
        this.silver = member.getSilver();
        this.gold = member.getGold();
        this.diamond = member.getDiamond();
        this.ruby = member.getRuby();
        this.platinum = member.getPlatinum();
        this.solvedBaekJoon = member.solvedCount();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.baekJoonName = member.getBaekJoonName();
        this.about = member.getAbout();
        this.profileImg = member.getProfileImg();
        this.provider = member.getProvider();
        this.email = member.getEmail();
        this.token = member.getToken();
        this.newMember = true;
        this.lastSolvedProblemId = member.getLastSolvedProblemId();
        this.ranking = member.getRanking();
    }

    @QueryProjection
    public MemberDto(Long id, LocalDateTime createDate, LocalDateTime modifyDate, int bronze, int silver, int gold, int diamond, int ruby, int platinum, String username, String nickname, String baekJoonName, String about, String profileImg, String provider, String email, String token, boolean newMember, int lastSolvedProblemId, Integer ranking) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.diamond = diamond;
        this.ruby = ruby;
        this.platinum = platinum;
        this.username = username;
        this.nickname = nickname;
        this.baekJoonName = baekJoonName;
        this.about = about;
        this.profileImg = profileImg;
        this.provider = provider;
        this.email = email;
        this.token = token;
        this.newMember = newMember;
        this.solvedBaekJoon = bronze + silver + gold + diamond + ruby + platinum;
        this.lastSolvedProblemId = lastSolvedProblemId;
        this.ranking = ranking;
    }
}
