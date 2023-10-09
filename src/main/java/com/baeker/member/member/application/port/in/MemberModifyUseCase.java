package com.baeker.member.member.application.port.in;

import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.reqDto.MyStudyReqDto;
import com.baeker.member.member.in.reqDto.SolvedCountReqDto;
import com.baeker.member.member.in.reqDto.UpdateLastSolvedReqDto;
import com.baeker.member.member.in.reqDto.UpdateReqDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberModifyUseCase {
    Member update(UpdateReqDto dto, MultipartFile img);
    Member updateProfile(UpdateReqDto dto);
    Member updateMyStudy(MyStudyReqDto dto);
    Member updateLastSolved(UpdateLastSolvedReqDto dto);
    Member updateImg(MultipartFile img, Long id);
    Member connectBaekjoon(Long id, String name);
    Member addSolvedCount(SolvedCountReqDto reqDto);
    void updateRanking();
}
