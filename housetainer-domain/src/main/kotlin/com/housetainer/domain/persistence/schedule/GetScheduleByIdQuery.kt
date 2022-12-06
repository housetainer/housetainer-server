package com.housetainer.domain.persistence.schedule

import com.housetainer.domain.entity.schedule.Schedule

interface GetScheduleByIdQuery {
    suspend fun getSchedule(scheduleId: String): Schedule?
}
