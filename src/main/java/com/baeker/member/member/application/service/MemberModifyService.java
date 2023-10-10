package com.baeker.member.member.application.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.baeker.member.base.error.exception.InvalidDuplicateException;
import com.baeker.member.base.error.exception.NotFoundException;
import com.baeker.member.base.request.RsData;
import com.baeker.member.base.s3.S3Config;
import com.baeker.member.member.application.port.in.MemberModifyUseCase;
import com.baeker.member.member.application.port.in.MemberQueryUseCase;
import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import com.baeker.member.member.application.port.out.persistence.SnapshotRepository;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.domain.entity.MemberSnapshot;
import com.baeker.member.member.in.event.ConBjEvent;
import com.baeker.member.member.in.event.CreateMyStudyEvent;
import com.baeker.member.member.in.reqDto.*;
import com.baeker.member.member.out.feign.SolvedAcClient;
import com.baeker.member.member.out.resDto.ConBaekjoonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberModifyUseCase {
    private final MemberRepositoryPort memberRepositoryPort;
    private final MemberQueryUseCase memberQueryUseCase;
    private final S3Config s3Config;
    private final AmazonS3 amazonS3;
    private final SnapshotRepository snapshotRepository;
    private final SolvedAcClient solvedAcClient;
    private final ApplicationEventPublisher publisher;


    @Override
    public Member update(UpdateReqDto dto, MultipartFile img) {
        Member member = memberQueryUseCase.findById(dto.getId());
        String imgUrl = this.s3Upload(img, dto.getId());

        Member updateMember = member.update(
                dto.getNickname(),
                dto.getAbout(),
                imgUrl
        );
        return memberRepositoryPort.save(updateMember);
    }

    @Override
    public Member updateProfile(UpdateReqDto dto) {
        Member member = memberQueryUseCase.findById(dto.getId())
                .updateProfile(
                        dto.getNickname(),
                        dto.getAbout()
                );
        return memberRepositoryPort.save(member);
    }

    @Override
    public Member updateMyStudy(MyStudyReqDto dto) {
        Member member = memberQueryUseCase.findById(dto.getMemberId());

        if (member.getMyStudies().contains(dto.getMyStudyId()))
            throw new InvalidDuplicateException("이미 등록된 my study / my study id = " + dto.getMyStudyId());

        return memberRepositoryPort.save(member.updateMyStudy(dto.getMyStudyId()));
    }

    @Override
    public Member updateLastSolved(UpdateLastSolvedReqDto dto) {
        Member member = memberQueryUseCase.findById(dto.getMemberId());
        return memberRepositoryPort.save(member.updateLastSolved(dto.getProblemId()));
    }

    @Override
    public Member updateImg(MultipartFile img, Long id) {
        Member member = memberQueryUseCase.findById(id);

        String profileImg = s3Upload(img, id);

        return memberRepositoryPort.save(member.updateProfileImg(profileImg));
    }

    @Override
    public Member connectBaekjoon(Long id, String name) {

        memberQueryUseCase.findById(id);
        RsData<ConBaekjoonResDto> resDto = solvedAcClient.validName(name);
        publisher.publishEvent(new ConBjEvent(this, id, name, resDto.getData()));
        return memberQueryUseCase.findById(id);
    }

    @Override
    public Member addSolvedCount(SolvedCountReqDto reqDto) {

        Member member = memberQueryUseCase.findById(reqDto.getId());

        if (member.getBaekJoonName() == null)
            throw new NotFoundException("백준 연동 아이디가 없는 회원입니다.");

        BaekJoonDto dto = new BaekJoonDto(reqDto);
        String today = LocalDateTime.now().plusDays(reqDto.getAdd()).getDayOfWeek().toString();
        this.updateSnapshot(member, dto, today);

        return memberRepositoryPort.save(member.updateSolvedCount(reqDto));
    }

    private void updateSnapshot(Member member, BaekJoonDto dto, String today) {
        List<MemberSnapshot> snapshots = member.getSnapshotList();

        if (snapshots.size() == 0 || !snapshots.get(snapshots.size() - 1).getDayOfWeek().equals(today)) {
            MemberSnapshot snapshot = MemberSnapshot.create(member, dto, today);
            snapshotRepository.save(snapshot);

        } else {
            MemberSnapshot snapshot = snapshots.get(snapshots.size() - 1).update(dto);
            snapshotRepository.save(snapshot);
        }

        if (snapshots.size() == 8) {
            MemberSnapshot snapshot = snapshots.get(0);
            snapshots.remove(snapshot);
            snapshotRepository.delete(snapshot);
        }
    }

    @Override
    public void updateRanking() {
        List<Member> memberList = memberRepositoryPort.findMemberRanking();

        for (int i = 0; i < memberList.size(); i++)
            memberList.get(i).updateRanking(i + 1);
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

    @Override
    public String conBj(ConBjEvent event) {

        Member member = memberQueryUseCase.findById(event.getId());

        try {
            memberQueryUseCase.findByBaekJoonName(event.getBaekJoonName());
            throw new InvalidDuplicateException(event.getBaekJoonName() + "은 이미 연동된 백준 id 입니다.");
        } catch (NotFoundException e) {
        }

        BaekJoonDto dto = new BaekJoonDto(event);
        String today = LocalDateTime.now().getDayOfWeek().toString();
        this.updateSnapshot(member, dto, today);

        Member updateMember = member.connectBaekJoon(event);
        return memberRepositoryPort.save(updateMember).getBaekJoonName();
    }

    public void createMyStudy(CreateMyStudyEvent event) {
        Member member = memberQueryUseCase.findById(event.getMemberId());
        member.addMyStudy(event.getMyStudyId());
    }
}
