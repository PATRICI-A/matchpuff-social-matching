package com.matchpuff.matchingservice.matching_service.infrastructure.messaging;


import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.matchpuff.matchingservice.matching_service.application.dto.event.MatchReceivedEventDto;
import com.matchpuff.matchingservice.matching_service.application.dto.event.MatchResponseEventDto;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchStatus;
import com.matchpuff.matchingservice.matching_service.domain.ports.out.MatchEventPublisherPort;

@Component
@RequiredArgsConstructor
public class RabbitMQFriendshipPublisher implements MatchEventPublisherPort{

    private final RabbitTemplate rabbitTemplate;


    @Override
    public void publishMatchReceived(UUID senderUserId, UUID receiverUserId, Double affinityPercentage) {
        MatchReceivedEventDto event = MatchReceivedEventDto.builder()
                .senderUserId(senderUserId)
                .receiverUserId(receiverUserId)
                .affinityPercentage(affinityPercentage)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        rabbitTemplate.convertAndSend("matching.exchange","match.received", event);
    }

    @Override
    public void publishMatchResponse(UUID senderUserId, UUID receiverUserId, MatchStatus status) {
        MatchResponseEventDto event = MatchResponseEventDto.builder()
                .senderUserId(senderUserId)
                .receiverUserId(receiverUserId)
                .status(status)
                .respondedAt(java.time.LocalDateTime.now())
                .build();
        rabbitTemplate.convertAndSend("matching.exchange","match.response", event);
    }
}
