package com.baeker.member.member.out.feign;

import com.baeker.member.base.error.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.InternalServerErrorException;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 400 :
                return new NotFoundException("feign: data 를 찾을 수 없습니다.");
            case  500 :
                return new InternalServerErrorException("서버 내부에 오류가 발생했습니다.");
        }
        return new Exception(response.reason());
    }
}
