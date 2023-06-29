package com.baeker.member.member.out.feign;

import com.baeker.member.base.request.RsData;
import com.baeker.member.member.out.resDto.ConBaekjoonResDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Qualifier
@FeignClient(name = "solved-ac", url = "${custom.feign.solved}")
public interface SolvedAcClient {

    @GetMapping("/v1/valid/{name}")
    RsData<ConBaekjoonResDto> validName(@PathVariable("name") String baekjoonName);
}
