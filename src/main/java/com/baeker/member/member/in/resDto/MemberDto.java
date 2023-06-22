package com.baeker.member.member.in.resDto;

import com.baeker.member.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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
    private String kakaoProfileImage;
    private String provider;
    private String email;
    private String token;
    private boolean newMember;

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
        this.kakaoProfileImage = member.getKakaoProfileImage();
        this.provider = member.getProvider();
        this.email = member.getEmail();
        this.token = member.getToken();
        this.newMember = true;
    }
}
