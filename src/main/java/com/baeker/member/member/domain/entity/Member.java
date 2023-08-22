package com.baeker.member.member.domain.entity;

import com.baeker.member.member.domain.Role;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import com.baeker.member.member.in.reqDto.SolvedCountReqDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;
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
    private String password;
    private String provider;
    private String email;
    private String token;
    private Integer ranking;
    private boolean newMember;
    @Enumerated(EnumType.STRING)
    private Role role;

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
                .about("")
                .password(dto.getPassword())
                .profileImg(dto.getProfileImage())
                .email(dto.getEmail())
                .role(Role.ROLE_USER)
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
                .modifyDate(now())
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

    // 카프카 대신 임시용 //
    public Member updateSolvedCount(SolvedCountReqDto dto) {
        return this.toBuilder()
                .bronze(this.getBronze() + dto.getBronze())
                .silver(this.getSilver() + dto.getSilver())
                .gold(this.getGold() + dto.getGold())
                .diamond(this.getDiamond() + dto.getDiamond())
                .ruby(this.getRuby() + dto.getRuby())
                .platinum(this.getPlatinum() + dto.getPlatinum())
                .build();
    }


    // nickname, about, profile img 수정 //
    public Member update(String nickname, String about, String profileImg) {
        return this.toBuilder()
                .nickname(nickname)
                .about(about)
                .profileImg(profileImg)
                .modifyDate(now())
                .build();
    }

    // my study 추가 //
    public void addMyStudy(Long myStudyId) {
        this.myStudies.add(myStudyId);
    }

    // my study update //
    public Member updateMyStudy(Long myStudyId) {
        Member member = this.toBuilder()
                .modifyDate(now())
                .build();

        member.myStudies.add(myStudyId);
        return member;
    }

    // profile img update //
    public Member updateProfileImg(String profileImg) {
        return this.toBuilder()
                .profileImg(profileImg)
                .modifyDate(now())
                .build();
    }

    // 최근 제출한 문제 update //
    public Member updateLastSolved(int problem) {
        return this.toBuilder()
                .lastSolvedProblemId(problem)
                .build();
    }

    // 랭킹 업데이트 //
    public void updateRanking(int rank) {
        this.ranking = rank;
    }


    /**
     * 추가한 부분
     */


    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 회원에게 member 권한 부여 //
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // admin 권한 부여 //
        if ("admin".equals(username))
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return grantedAuthorities;
    }

    public Map<String, Object> toClaims() {
        return Map.of(
                "id", getId(),
                "username", getUsername(),
                "role", getRole()
        );
    }
}