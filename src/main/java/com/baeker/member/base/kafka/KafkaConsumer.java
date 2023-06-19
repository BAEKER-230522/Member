package com.baeker.member.base.kafka;

import com.baeker.member.base.exception.NotFoundException;
import com.baeker.member.member.in.event.AddSolvedCountEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Slf4j
//@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ApplicationEventPublisher publisher;

    @KafkaListener(topics = "${message.topic.solved-member}", groupId = GROUP_ID_CONFIG)
    public void bySolved(String message) throws IOException {
        log.debug("############# 메시지 대기중 ###############");
        log.info("message : {}", message);

        Map<Object, Object> map;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            map = objectMapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new NotFoundException(e.getMessage() + "데이터 없음");
        }

        try {
            Integer memberId = (Integer) map.get("id");
            Long longId = memberId.longValue();
            int bronze = (int) map.get("bronze");
            int silver = (int) map.get("silver");
            int gold = (int) map.get("gold");
            int platinum = (int) map.get("platinum");
            int diamond = (int) map.get("diamond");
            int ruby = (int) map.get("ruby");

            publisher.publishEvent(new AddSolvedCountEvent(this,
                    longId, bronze, silver, gold, diamond, ruby, platinum));
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Member data 없음");
        }
    }
}
