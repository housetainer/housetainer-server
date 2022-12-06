package com.housetainer.domain.model.supporter


import com.housetainer.domain.entity.schedule.AccessType
import com.housetainer.domain.entity.schedule.Schedule
import com.housetainer.domain.entity.schedule.ScheduleType
import com.housetainer.domain.model.schedule.CreateScheduleRequest
import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy

@Builder(builderStrategy = ExternalStrategy, forClass = InternalCreateScheduleRequest)
class CreateScheduleRequestBuilder {

    private CreateScheduleRequestBuilder() {}

    static def create(
        String title,
        String content,
        Long date,
        String category
    ) {
        new CreateScheduleRequestBuilder()
            .title(title)
            .content(content)
            .date(date)
            .category(category)
    }

    static def create(Schedule schedule) {
        create(schedule.title, schedule.content, schedule.date, schedule.category)
            .type(schedule.type)
            .accessType(schedule.accessType)
            .visible(schedule.visible)
            .photo(schedule.photo)
            .relatedLink(schedule.relatedLink)
            .parentScheduleId(schedule.parentSchedule?.scheduleId)
    }

    CreateScheduleRequest toCreateScheduleRequest() {
        def result = build()
        new CreateScheduleRequest(
            result.title,
            result.content,
            result.date,
            result.category,
            result.type,
            result.accessType,
            result.visible,
            result.photo,
            result.relatedLink,
            result.parentScheduleId
        )
    }

    private class InternalCreateScheduleRequest {
        String title
        String content
        Long date
        String category
        ScheduleType type
        AccessType accessType
        Boolean visible
        String photo
        String relatedLink
        String parentScheduleId
    }
}
