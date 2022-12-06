package com.housetainer.application.schedule

import com.housetainer.common.utils.CommonUtils
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.schedule.Schedule
import com.housetainer.domain.model.schedule.CreateScheduleRequest
import com.housetainer.domain.persistence.schedule.CreateScheduleCommand
import com.housetainer.domain.persistence.schedule.GetScheduleByIdQuery
import com.housetainer.domain.port.token.TokenService
import com.housetainer.domain.usecase.schedule.CreateScheduleUseCase
import com.housetainer.domain.usecase.schedule.GetScheduleUseCase
import java.time.Instant

class ScheduleService(
    private val getScheduleByIdQuery: GetScheduleByIdQuery,
    private val createScheduleCommand: CreateScheduleCommand
) : CreateScheduleUseCase,
    GetScheduleUseCase {

    override suspend fun createSchedule(token: String, request: CreateScheduleRequest): Schedule {
        val tokenInformation = TokenService.validateToken(token)

        val parentSchedule: Schedule? = request.parentScheduleId
            ?.let {
                getScheduleByIdQuery.getSchedule(it) ?: throw parentScheduleNotFoundException()
            }

        val now = Instant.now().toEpochMilli()
        val schedule = Schedule(
            scheduleId = CommonUtils.randomUuid,
            userId = tokenInformation.userId,
            parentSchedule = parentSchedule,
            title = request.title,
            content = request.content,
            date = request.date,
            category = request.category,
            type = request.type,
            accessType = request.accessType,
            visible = request.visible,
            photo = request.photo,
            relatedLink = request.relatedLink,
            createTime = now,
            updateTime = now
        )

        return createScheduleCommand.createSchedule(schedule)
    }

    override suspend fun getSchedule(scheduleId: String): Schedule {
        return getScheduleByIdQuery.getSchedule(scheduleId) ?: throw scheduleNotFoundException()
    }

    companion object {
        @JvmStatic
        fun scheduleNotFoundException() = BaseException(404, "schedule not found")

        @JvmStatic
        fun parentScheduleNotFoundException() = BaseException(404, "parent schedule not found")
    }
}
