package com.housetainer.application.schedule

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.schedule.AccessType
import com.housetainer.domain.entity.schedule.Schedule
import com.housetainer.domain.entity.schedule.ScheduleType
import com.housetainer.domain.model.auth.InternalIssueTokenRequest
import com.housetainer.domain.model.supporter.CreateScheduleRequestBuilder
import com.housetainer.domain.model.supporter.ScheduleBuilder
import com.housetainer.domain.persistence.schedule.CreateScheduleCommand
import com.housetainer.domain.persistence.schedule.GetScheduleByIdQuery

import java.time.Instant

class ScheduleServiceSpec extends ApplicationSpecification {

    GetScheduleByIdQuery getScheduleByIdQuery = Mock()
    CreateScheduleCommand createScheduleCommand = Mock()

    ScheduleService sut

    String userId
    String token

    def setup() {
        sut = new ScheduleService(
            getScheduleByIdQuery,
            createScheduleCommand
        )
        userId = uuid
        token = tokenService.issueToken(new InternalIssueTokenRequest(
            userId, uuid, AuthProvider.NAVER
        ))
    }

    def "create schedule"() {
        given:
        def schedule = createScheduleBuilder(userId).toSchedule()
        def request = CreateScheduleRequestBuilder.create(schedule)
            .toCreateScheduleRequest()

        when:
        sut.createSchedule(token, request, coroutineContext)

        then:
        1 * createScheduleCommand.createSchedule({ Schedule it ->
            it.userId == schedule.userId
            it.parentSchedule == schedule.parentSchedule
            it.title == schedule.title
            it.content == schedule.content
            it.date == schedule.date
            it.category == schedule.category
            it.type == schedule.type
            it.accessType == schedule.accessType
            it.visible == schedule.visible
            it.photo == schedule.photo
            it.relatedLink == schedule.relatedLink
            it.createTime == it.updateTime
            it.createTime > 0L
        }, _) >> schedule
        0 * getScheduleByIdQuery.getSchedule(*_)
        0 * _
    }

    def "create schedule with parent"() {
        given:
        def parentSchedule = createScheduleBuilder(userId).toSchedule()
        def schedule = createScheduleBuilder(userId)
            .parentSchedule(parentSchedule)
            .toSchedule()
        def request = CreateScheduleRequestBuilder.create(schedule)
            .toCreateScheduleRequest()

        when:
        sut.createSchedule(token, request, coroutineContext)

        then:
        1 * createScheduleCommand.createSchedule({ Schedule it ->
            it.userId == schedule.userId
            it.parentSchedule?.scheduleId == schedule.parentSchedule?.scheduleId
            it.title == schedule.title
            it.content == schedule.content
            it.date == schedule.date
            it.category == schedule.category
            it.type == schedule.type
            it.accessType == schedule.accessType
            it.visible == schedule.visible
            it.photo == schedule.photo
            it.relatedLink == schedule.relatedLink
            it.createTime == it.updateTime
            it.createTime > 0L
        }, _) >> schedule
        1 * getScheduleByIdQuery.getSchedule(parentSchedule.scheduleId, _) >> parentSchedule
        0 * _
    }

    def "create schedule with but parent schedule not found"() {
        given:
        def parentSchedule = createScheduleBuilder(userId).toSchedule()
        def schedule = createScheduleBuilder(userId)
            .parentSchedule(parentSchedule)
            .toSchedule()
        def request = CreateScheduleRequestBuilder.create(schedule)
            .toCreateScheduleRequest()

        when:
        sut.createSchedule(token, request, coroutineContext)

        then:
        def exception = thrown(BaseException)
        exception == ScheduleService.parentScheduleNotFoundException()
        1 * getScheduleByIdQuery.getSchedule(parentSchedule.scheduleId, _) >> null
        0 * _
    }

    def "get a schedule"() {
        given:
        def schedule = createScheduleBuilder(userId).toSchedule()

        when:
        def result = sut.getSchedule(schedule.scheduleId, coroutineContext) as Schedule

        then:
        result == schedule
        1 * getScheduleByIdQuery.getSchedule(schedule.scheduleId, _) >> schedule
        0 * _
    }

    def "get a schedule but not found"() {
        given:
        def schedule = createScheduleBuilder(userId).toSchedule()

        when:
        sut.getSchedule(schedule.scheduleId, coroutineContext)

        then:
        def exception = thrown(BaseException)
        exception == ScheduleService.scheduleNotFoundException()
        1 * getScheduleByIdQuery.getSchedule(schedule.scheduleId, _) >> null
        0 * _
    }

    def createScheduleBuilder(String userId) {
        ScheduleBuilder
            .create(
                uuid,
                userId,
                "title",
                "content",
                (Instant.now() + 5000).toEpochMilli(),
                "category"
            )
            .type(ScheduleType.NORMAL)
            .accessType(AccessType.PUBLIC)
            .visible(true)
            .createTime(Instant.now().toEpochMilli())
            .updateTime(Instant.now().toEpochMilli())
    }
}
