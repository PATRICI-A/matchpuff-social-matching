package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.CareerEnum;
import com.matchpuff.matchingservice.matching_service.domain.model.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "user_match_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMatchProfileDocument {
    @Id
    private UUID id;
    private CareerEnum career;
    private Integer semester;
    private List<TagDocument> tags;
    private List<ScheduleDocument> schedules;
    private GenderEnum gender;
    private List<GenderEnum> genderPreferences;
    private Date lastSync;
}
