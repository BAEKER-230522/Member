package com.baeker.member.member.domain.service;


import com.baeker.member.baekerSearch.domain.service.BaekerSearchService;
import com.baeker.member.base.request.RsData;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.reqDto.JoinReqDto;
import com.baeker.member.member.in.resDto.MemberDto;
import com.baeker.member.baekerSearch.in.resDto.SearchResDto;
import com.baeker.member.baekerSearch.in.resDto.StudyDto;
import com.baeker.member.member.out.feign.StudyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class BaekerSearchServiceTest {

    @Autowired
    private BaekerSearchService baekerSearchService;
    @Autowired
    private MemberService memberService;
    @MockBean
    private StudyClient studyClient;

    @BeforeEach
    public void beforeEach() {

        List<StudyDto> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            StudyDto dto = new StudyDto();
            dto.setId((long) i);
            dto.setName("study" + i);
            list.add(dto);
        }

        when(studyClient.findInput(eq("study"), anyInt(), anyInt()))
                .thenReturn(new RsData<>("S-1", "성공", list));

        when(studyClient.findInput(argThat(s -> !s.equals("study")), anyInt(), anyInt()))
                .thenReturn(new RsData<>("S-1", "성공", new ArrayList<>()));
    }

    @Test
    @DisplayName("검색어로 member, study 조회")
    public void no1() {
        Member member1 = member("user1", "abc");
        Member member2 = member("user2", "bcd");
        Member member3 = member("user3", "cde");
        Member member4 = member("user4", "def");
        Member member5 = member("user5", "efg");
        Member member6 = member("user6", "fgh");


        SearchResDto searchC = baekerSearchService
                .findByInput("c", 0, 10);
        List<MemberDto> membersC = searchC.getMembers();
        List<StudyDto> studiesC = searchC.getStudies();

        assertThat(membersC.size()).isEqualTo(3);
        assertThat(membersC.get(0).getNickname()).isEqualTo("abc");
        assertThat(studiesC.size()).isEqualTo(0);

        SearchResDto searchStudy = baekerSearchService
                .findByInput("study", 0, 10);
        List<MemberDto> membersStudy = searchStudy.getMembers();
        List<StudyDto> studiesStudy = searchStudy.getStudies();

        assertThat(membersStudy.size()).isEqualTo(0);
        assertThat(studiesStudy.size()).isEqualTo(3);
        assertThat(studiesStudy.get(0).getName()).isEqualTo("study1");
    }

    private Member member(String usr, String nickname) {
        JoinReqDto reqDto = JoinReqDto.createJoinDto(usr, nickname, "1234", "BAEKER", "aaa@aa.com", "123", "img");
        return memberService.create(reqDto);
    }
}