package com.matchpuff.matchingservice.matching_service.domain.model;

import com.matchpuff.matchingservice.matching_service.domain.model.enums.CareerEnum;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserMatchProfile {
    private UUID id;
    private CareerEnum career;
    private Integer semester;
    private List<String> tag;
    private List<String> schedule;
    private Date lastSync;
}
