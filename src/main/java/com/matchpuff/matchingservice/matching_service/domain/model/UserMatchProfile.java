package com.matchpuff.matchingservice.matching_service.domain.model;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.CareerEnum;
import com.matchpuff.matchingservice.matching_service.domain.model.enums.GenderEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class UserMatchProfile {
    private UUID id;
    private CareerEnum career;
    private Integer semester;
    private List<Tag> tags;
    private List<Schedule> schedules;
    private GenderEnum gender;
    private List<GenderEnum> genderPreferences;
    private Date lastSync;
}
