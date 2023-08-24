package com.baeker.member.member.out.feign;

import com.baeker.member.base.request.RsData;
import com.baeker.member.baekerSearch.in.resDto.StudyDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Qualifier
@FeignClient(name = "study", url = "${custom.feign.study}", configuration = FeignConfiguration.class)
public interface StudyClient {

    @GetMapping("/v1/{input}")
    RsData<List<StudyDto>> findInput(
            @PathVariable("input") String input,
            @RequestParam int page,
            @RequestParam int content
    );
}
