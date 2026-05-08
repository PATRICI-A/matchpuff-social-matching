package com.matchpuff.matchingservice.matching_service.application.dto.response;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.CareerEnum;
import com.matchpuff.matchingservice.matching_service.domain.model.enums.GenderEnum;
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
    private List<TagResponse> tags;
    private List<ScheduleResponse> schedules;
    private GenderEnum gender;
    private List<GenderEnum> genderPreferences;
    private Date lastSync;
}
