package com.baeker.member.member.domain.entity;

import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;
    private String nickname;
    private String about;
    private String profileImg;
    private String kakaoProfileImage;
    private String password;
    private String provider;
    private String email;
    private String token;
    private boolean newMember;

    @Builder.Default
    @ElementCollection
    private List<Long> myStudies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<MemberSnapshot> snapshotList = new ArrayList<>();

    //-- crate method --//
    public static Member createMember(JoinReqDto dto) {
        return builder()
                .provider(dto.getProvider())
                .username(dto.getUsername())
                .nickname(dto.getNickName())
                .about(dto.getToken())
                .password(dto.getPassword())
                .profileImg(dto.getProfileImage())
                .kakaoProfileImage(dto.getProfileImage())
                .email(dto.getEmail())
                .token(dto.getToken())
                .newMember(true)
                .build();
    }


    //-- business logic --//

    // name, about, profileImg 수정 //
    public Member modifyMember(String name, String about, String img) {
        return this.toBuilder()
                .nickname(name)
                .about(about)
                .profileImg(img)
                .modifyDate(LocalDateTime.now())
                .newMember(false)
                .build();
    }

    // 첫방문 회원 체크 //
    public void joinComplete() {
        this.newMember = false;
    }

    // 백준 아이디 등록 //
    public Member connectBaekJoon(ConBjEvent event) {
        return this.toBuilder()
                .baekJoonName(event.getBaekJoonName())
                .bronze(event.getBronze())
                .silver(event.getSilver())
                .gold(event.getGold())
                .diamond(event.getDiamond())
                .ruby(event.getRuby())
                .platinum(event.getPlatinum())
                .build();
    }

    // 백준 점수 최신화 //
    public Member updateSolvedCount(AddSolvedCountEvent event) {
        return this.toBuilder()
                .bronze(this.getBronze() + event.getBronze())
                .silver(this.getSilver() + event.getSilver())
                .gold(this.getGold() + event.getGold())
                .diamond(this.getDiamond() + event.getDiamond())
                .ruby(this.getRuby() + event.getRuby())
                .platinum(this.getPlatinum() + event.getPlatinum())
                .build();
    }

    // nickname, about, profile img 수정 //
    public Member update(String nickname, String about, String profileImg) {
        return this.toBuilder()
                .nickname(nickname)
                .about(about)
                .profileImg(profileImg)
                .build();
    }

    // my study 추가 //
    public void addMyStudy(Long myStudyId) {
        this.myStudies.add(myStudyId);
    }

    // my study update //
    public Member updateMyStudy(Long myStudyId) {
        this.myStudies.add(myStudyId);
        return this;
    }
}