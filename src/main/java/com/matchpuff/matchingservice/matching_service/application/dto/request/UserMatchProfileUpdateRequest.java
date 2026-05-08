package com.matchpuff.matchingservice.matching_service.application.dto.request;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.CareerEnum;
import com.matchpuff.matchingservice.matching_service.domain.model.enums.GenderEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserMatchProfileUpdateRequest {

    private CareerEnum career;

    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 10, message = "El semestre máximo es 10")
    private Integer semester;

    @Valid
    private List<TagRequest> tags;

    @Valid
    private List<ScheduleRequest> schedules;

    private GenderEnum gender;

    private List<GenderEnum> genderPreferences;

    @Past(message = "La fecha de última sincronización debe ser en el pasado")
    private Date lastSync;
}
