package com.baeker.member.baekerSearch.domain.service;

import com.baeker.member.baekerSearch.in.resDto.SearchResDto;
import com.baeker.member.member.application.port.out.persistence.MemberRepositoryPort;
import com.baeker.member.member.out.feign.StudyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BaekerSearchService {

    private final MemberRepositoryPort memberRepositoryPort;
    private final StudyClient studyClient;

    //-- 검색어로 member, study 검색 --//
    public SearchResDto findByInput(String input, int page, int content) {
        SearchResDto dto = new SearchResDto();

        dto.setMembers(memberRepositoryPort.findByInput(input, page, content));
        dto.setStudies(studyClient.findInput(input, page, content).getData());

        return dto;
    }
}
