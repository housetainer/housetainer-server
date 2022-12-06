package com.housetainer.domain.persistence.schedule

import com.housetainer.domain.entity.schedule.Schedule

interface CreateScheduleCommand {
    suspend fun createSchedule(schedule: Schedule): Schedule
}
