package com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.mapper;

import com.matchpuff.matchingservice.matching_service.domain.model.Schedule;
import com.matchpuff.matchingservice.matching_service.domain.model.Tag;
import com.matchpuff.matchingservice.matching_service.domain.model.UserMatchProfile;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.ScheduleDocument;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.TagDocument;
import com.matchpuff.matchingservice.matching_service.infrastructure.adapters.persistence.entity.UserMatchProfileDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMatchProfilePersistenceMapper {

    UserMatchProfileDocument toDocument(UserMatchProfile profile);

    UserMatchProfile toDomain(UserMatchProfileDocument document);

    TagDocument toTagDocument(Tag tag);

    // Tag tiene constructor con validacion, se instancia manualmente
    default Tag toTag(TagDocument doc) {
        return new Tag(doc.getName(), doc.getCategory());
    }

    ScheduleDocument toScheduleDocument(Schedule schedule);

    // Schedule tiene constructor con validacion, se instancia manualmente
    default Schedule toSchedule(ScheduleDocument doc) {
        return new Schedule(doc.getDayOfWeek(), doc.getName(), doc.getStartTime(), doc.getEndTime());
    }
}
