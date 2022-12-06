package com.housetainer.domain.usecase.schedule

import com.housetainer.domain.entity.schedule.Schedule

interface GetScheduleUseCase {

    suspend fun getSchedule(scheduleId: String): Schedule
}
