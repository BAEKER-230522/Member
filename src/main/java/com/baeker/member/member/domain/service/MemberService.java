package com.baeker.member.member.domain.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.baeker.member.base.exception.InvalidDuplicateException;
import com.baeker.member.base.exception.NotFoundException;
import com.baeker.member.base.request.RsData;
import com.baeker.member.base.s3.S3Config;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.*;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import com.baeker.member.member.in.resDto.SnapshotQueryRepository;
import com.baeker.member.member.out.MemberQueryRepository;
import com.baeker.member.member.out.MemberRepository;
import com.baeker.member.member.out.SnapshotRepository;
import com.baeker.member.member.out.feign.SolvedAcClient;
import com.baeker.member.member.out.resDto.ConBaekjoonResDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final SnapshotRepository snapshotRepository;
    private final SnapshotQueryRepository snapshotQueryRepository;
    private final ApplicationEventPublisher publisher;
    private final SolvedAcClient solvedAcClient;
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;


    /**
     * * CREATE METHOD **
     * member 객체 생성
     * s3 upload
     */

    //-- create member --//
    @Transactional
    public Member create(JoinReqDto dto) {

        try {
            this.findByUsername(dto.getUsername());
            throw new InvalidDuplicateException("이미 존재하는 username 입니다.");
        } catch (NotFoundException e) {
        }

        Member member = Member.createMember(dto);

        return memberRepository.save(member);
    }


    /**
     * * READ METHOD **
     * find by username
     * find all
     * find all + paging
     * find by id
     * find by member id list
     * find by 백준 name
     * find all snapshot
     * find today snapshot
     */

    //-- find by username --//
    public Member findByUsername(String username) {
        Optional<Member> byUsername = memberRepository.findByUsername(username);

        if (byUsername.isPresent())
            return byUsername.get();

        throw new NotFoundException("존재하지 않는 username");
    }

    //-- find all --//
    public List<Member> finAll() {
        return memberRepository.findAll();
    }

    //-- find all + paging --//
    public Page<Member> getAll(PageReqDto dto) {
        ArrayList<Sort.Order> sorts = new ArrayList<>();

        if (dto.isAsc())
            sorts.add(Sort.Order.asc(dto.getStandard()));
        else
            sorts.add(Sort.Order.desc(dto.getStandard()));

        Pageable pageable = PageRequest.of(
                dto.getPage(),
                dto.getMaxContent(),
                Sort.by(sorts)
        );
        return memberRepository.findAll(pageable);
    }

    //-- find all 백준 연동한 사람만 --//
    public List<SchedulerResDto> findAllConBJ() {
        List<SchedulerResDto> memberList = memberQueryRepository.findAllConBJ();

        if (memberList.size() == 0)
            throw new NotFoundException("백준 연동된 회원이 없습니다.");

        return memberList;
    }

    //-- find by id --//
    public Member findById(Long id) {
        Optional<Member> byId = memberRepository.findById(id);

        if (byId.isPresent())
            return byId.get();

        throw new NotFoundException("존재하지 않는 id / id = " + id);
    }

    //-- find by member id list --//
    public List<MemberDto> findByMyStudyList(List<Long> memberIds, String status) {
        return memberQueryRepository.findByMemberList(memberIds, status);
    }

    //-- find by 백준 name --//
    public Member findByBaekJoonName(String baekJoonName) {
        Optional<Member> byBaekJoonName = memberRepository.findByBaekJoonName(baekJoonName);

        if (byBaekJoonName.isPresent())
            return byBaekJoonName.get();

        throw new NotFoundException("존재하지 않는 백준 name / name = " + baekJoonName);
    }

    //-- find all snapshot --//
    public List<MemberSnapshot> findAllSnapshot(Member member) {
        return snapshotQueryRepository.findByMemberId(member);
    }

    //-- find today snapshot --//
    public MemberSnapshot findTodaySnapshot(Member member) {
        List<MemberSnapshot> snapshots = member.getSnapshotList();

        if (snapshots.size() == 0)
            throw new NotFoundException("백준 연동이 되어있지 않은 회원 입니다.");

        return member.getSnapshotList().get(0);
    }


    /**
     * * UPDATE METHOD **
     * nickname, about, profile img 수정
     * update my study
     * delete my study
     * update lastSolvedProblemId
     * update profile img
     * event : 백준 연동
     * evnet : Solved Count Update
     * kafka 임시 대용 : Solved Count Update
     * event : when create my study
     * update snapshot
     * s3 upload
     */

    //-- nickname, about, profile img 수정 --//
    @Transactional
    public Member update(UpdateReqDto dto) {

        Member member = this.findById(dto.getId())
                .update(
                        dto.getNickname(),
                        dto.getAbout(),
                        dto.getProfileImg()
                );

        return memberRepository.save(member);
    }

    //-- update my study --//
    @Transactional
    public Member updateMyStudy(MyStudyReqDto dto) {
        Member member = this.findById(dto.getMemberId());

        if (member.getMyStudies().contains(dto.getMyStudyId()))
            throw new InvalidDuplicateException("이미 등록된 my study / my study id = " + dto.getMyStudyId());

        return memberRepository.save(member.updateMyStudy(dto.getMyStudyId()));
    }

    //-- delete my study --//
    @Transactional
    public Member deleteMyStudy(MyStudyReqDto dto) {
        Member member = this.findById(dto.getMemberId());

        if (!member.getMyStudies().contains(dto.getMyStudyId()))
            throw new NotFoundException("존재하지 않는 my study id / id = " + dto.getMyStudyId());

        member.getMyStudies().remove(dto.getMyStudyId());
        return member;
    }

    //-- update lastSolvedProblemId --//
    @Transactional
    public Member updateLastSolved(UpdateLastSolvedReqDto dto) {
        Member member = this.findById(dto.getMemberId());
        return memberRepository.save(member.updateLastSolved(dto.getProblemId()));
    }


    //-- update profile img --//
    @Transactional
    public Member updateImg(MultipartFile img, Long id) {
        Member member = this.findById(id);

        String profileImg = s3Upload(img, id);

        return memberRepository.save(member.updateProfileImg(profileImg));
    }

    @Transactional
    public Member connectBaekjoon(Long id, String name) {

        try {
            RsData<ConBaekjoonResDto> resDto = solvedAcClient.validName(name);
            publisher.publishEvent(new ConBjEvent(this, id, name, resDto.getData()));
            return this.findById(id);
        } catch (FeignException e) {
            throw new NotFoundException("존재하지 않는 백준 이름입니다.");
        }
    }

    //-- event : 백준 연동 --//
    public String conBj(ConBjEvent event) {

        Member member = this.findById(event.getId());

        try {
            this.findByBaekJoonName(event.getBaekJoonName());
            throw new InvalidDuplicateException(event.getBaekJoonName() + "은 이미 연동된 백준 id 입니다.");
        } catch (NotFoundException e) {
        }

        BaekJoonDto dto = new BaekJoonDto(event);
        this.updateSnapshot(member, dto);

        Member updateMember = member.connectBaekJoon(event);
        return memberRepository.save(updateMember).getBaekJoonName();
    }

    //-- event : solved count update --//
    public void addSolvedCount(AddSolvedCountEvent event) {

        Member member = this.findById(event.getId());

        BaekJoonDto dto = new BaekJoonDto(event);
        this.updateSnapshot(member, dto);

        memberRepository.save(member.updateSolvedCount(event));
    }

    //-- kafka 대신 임시용 api --//
    @Transactional
    public Member addSolvedCount(SolvedCountReqDto reqDto) {

        Member member = this.findById(reqDto.getId());

        if (member.getBaekJoonName() == null)
            throw new NotFoundException("백준 연동 아이디가 없는 회원입니다.");

        BaekJoonDto dto = new BaekJoonDto(reqDto);
        this.updateSnapshot(member, dto);

        return memberRepository.save(member.updateSolvedCount(reqDto));
    }

    //-- event : when create my study --//
    public void createMyStudy(CreateMyStudyEvent event) {
        Member member = this.findById(event.getMemberId());
        member.addMyStudy(event.getMyStudyId());
    }

    // update snapshot //
    private void updateSnapshot(Member member, BaekJoonDto dto) {
        String today = LocalDateTime.now().getDayOfWeek().toString();
        List<MemberSnapshot> snapshots = member.getSnapshotList();

        if (snapshots.size() == 0 || !snapshots.get(0).getDayOfWeek().equals(today)) {
            MemberSnapshot snapshot = MemberSnapshot.create(member, dto, today);
            snapshotRepository.save(snapshot);

        } else {
            MemberSnapshot snapshot = snapshots.get(0).update(dto);
            snapshotRepository.save(snapshot);
        }

        if (snapshots.size() == 8) {
            MemberSnapshot snapshot = snapshots.get(7);
            snapshots.remove(snapshot);
            snapshotRepository.delete(snapshot);
        }
    }

    // S3 upload //
    private String s3Upload(MultipartFile file, Long id) {

        String name = "profile_img" + id;
        String url = "https://s3." + s3Config.getRegion()
                + ".amazonaws.com/" + s3Config.getBucket()
                + "/" + s3Config.getStorage()
                + "/" + name;

        try {
            ObjectMetadata data = new ObjectMetadata();
            data.setContentType(file.getContentType());
            data.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(
                    s3Config.getBucket(),
                    s3Config.getStorage() + "/" + name,
                    file.getInputStream(),
                    data
            ));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw new NullPointerException("프로필 이미지가 없습니다.");
        }
        return url;
    }

    /**
     * 소셜 로그인 추가
     * @param providerType
     * @param username
     * @param email
     * @return
     */
    public Member whenSocialLogin(String providerType, String username, String email, String profileImg) {

        Member member = null;
        try {
            member = findByUsername(username);
        } catch (NotFoundException e) {
            return socialJoin(providerType, username, "", email, profileImg); // 최초 1회 실행
        }
        return member;
    }


    @Transactional
    public Member socialJoin(String providerType, String username, String password, String email, String profileImg) {
        Member member = null;

//        try {
//            member = findByUsername(username);
//            return member;
//        } catch (NotFoundException e) {
            JoinReqDto dto = new JoinReqDto();
            dto.setUsername(username);
            dto.setEmail(email);
            dto.setPassword(password);
//            JoinReqDto dto = JoinReqDto.createJoinDto(username, ,password, email);
            dto.setProvider(providerType);
            dto.setProfileImage(profileImg);
//            dto.setToken(token);
            dto.setNickName(username);

            member = create(dto);
//        }

        memberRepository.save(member);

        return member;
    }
}
