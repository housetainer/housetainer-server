package com.housetainer.domain.usecase.schedule

import com.housetainer.domain.entity.schedule.Schedule
import com.housetainer.domain.model.schedule.CreateScheduleRequest

interface CreateScheduleUseCase {

    suspend fun createSchedule(token: String, request: CreateScheduleRequest): Schedule
}
