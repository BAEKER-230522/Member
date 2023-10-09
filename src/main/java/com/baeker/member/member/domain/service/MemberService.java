package com.baeker.member.member.domain.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.baeker.member.base.error.exception.InvalidDuplicateException;
import com.baeker.member.base.error.exception.NotFoundException;
import com.baeker.member.base.request.RsData;
import com.baeker.member.base.s3.S3Config;
import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import com.baeker.member.member.application.port.out.persistence.SnapshotRepository;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.*;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.member.in.resDto.SchedulerResDto;
import com.baeker.member.member.in.resDto.SnapshotQueryRepository;
import com.baeker.member.member.out.feign.SolvedAcClient;
import com.baeker.member.member.out.resDto.ConBaekjoonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
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

    private final MemberRepositoryPort memberRepositoryPort;
    private final SnapshotRepository snapshotRepository;
    private final SnapshotQueryRepository snapshotQueryRepository;
    private final ApplicationEventPublisher publisher;
    private final SolvedAcClient solvedAcClient;
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;



    /**
     * * UPDATE METHOD **
     * nickname, about, profile img 수정
     * nickname, about 수정
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
     * ranking 수동 업데이트
     */



    //-- event : 백준 연동 --//
    public String conBj(ConBjEvent event) {

        Member member = this.findById(event.getId());

        try {
            this.findByBaekJoonName(event.getBaekJoonName());
            throw new InvalidDuplicateException(event.getBaekJoonName() + "은 이미 연동된 백준 id 입니다.");
        } catch (NotFoundException e) {
        }

        BaekJoonDto dto = new BaekJoonDto(event);
        String today = LocalDateTime.now().getDayOfWeek().toString();
        this.updateSnapshot(member, dto, today);

        Member updateMember = member.connectBaekJoon(event);
        return memberRepositoryPort.save(updateMember).getBaekJoonName();
    }


    //-- kafka 대신 임시용 api --//


    //-- event : when create my study --//
    public void createMyStudy(CreateMyStudyEvent event) {
        Member member = this.findById(event.getMemberId());
        member.addMyStudy(event.getMyStudyId());
    }

    // test 용 update snapshot //
    public void updateSnapshotTest(Member member, BaekJoonDto dto, String today) {
        this.updateSnapshot(member, dto, today);
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
     *
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
}
