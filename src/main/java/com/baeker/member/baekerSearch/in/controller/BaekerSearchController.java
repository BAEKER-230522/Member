package com.baeker.member.baekerSearch.in.controller;

import com.baeker.member.base.request.RsData;
import com.baeker.member.baekerSearch.domain.service.BaekerSearchService;
import com.baeker.member.baekerSearch.in.resDto.SearchResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class BaekerSearchController {

    private final BaekerSearchService baekerSearchService;

    @Operation(summary = "검색어로 study, member 검색")
    @GetMapping("/v1/{input}")
    public RsData<SearchResDto> findByInput(
            @PathVariable String input,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int content
    ) {
        log.info("검색어로 study, member 검색요청 확인 input = {}, page = {}, contetn = {}", input, page, content);

        SearchResDto dto = baekerSearchService.findByInput(input, page, content);

        log.info("검색어로 study, member 검색응답 완료");
        return RsData.successOf(dto);
    }

}
