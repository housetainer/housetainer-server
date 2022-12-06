package com.housetainer.domain.model.supporter


import com.housetainer.domain.entity.schedule.AccessType
import com.housetainer.domain.entity.schedule.Schedule
import com.housetainer.domain.entity.schedule.ScheduleType
import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy

@Builder(builderStrategy = ExternalStrategy, forClass = InternalSchedule)
class ScheduleBuilder {

    private ScheduleBuilder() {}

    static def create(
        String scheduleId,
        String userId,
        String title,
        String content,
        Long date,
        String category
    ) {
        new ScheduleBuilder()
            .scheduleId(scheduleId)
            .userId(userId)
            .title(title)
            .content(content)
            .date(date)
            .category(category)
    }

    Schedule toSchedule() {
        def result = build()
        new Schedule(
            result.scheduleId,
            result.userId,
            result.parentSchedule,
            result.title,
            result.content,
            result.date,
            result.category,
            result.type,
            result.accessType,
            result.visible,
            result.photo,
            result.relatedLink,
            result.createTime ?: 0L,
            result.updateTime ?: 0L
        )
    }

    private class InternalSchedule {
        String scheduleId
        String userId
        Schedule parentSchedule
        String title
        String content
        Long date
        String category
        ScheduleType type
        AccessType accessType
        Boolean visible
        String photo
        String relatedLink
        Long createTime
        Long updateTime
    }
}
