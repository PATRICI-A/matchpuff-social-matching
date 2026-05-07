package com.matchpuff.matchingservice.matching_service.application.dto.response;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.CareerEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserMatchProfileResponse {
    private UUID id;
    private CareerEnum career;
    private Integer semester;
    private List<String> tag;
    private List<String> schedule;
    private Date lastSync;
}
