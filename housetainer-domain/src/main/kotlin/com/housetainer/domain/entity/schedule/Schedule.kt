package com.housetainer.domain.entity.schedule

data class Schedule(
    val scheduleId: String,
    val userId: String,
    var parentSchedule: Schedule?,
    val title: String,
    val content: String,
    val date: Long,
    val category: String,
    val type: ScheduleType = ScheduleType.NORMAL,
    val accessType: AccessType = AccessType.PUBLIC,
    val visible: Boolean = true,
    val photo: String? = null,
    val relatedLink: String? = null,
    val createTime: Long,
    val updateTime: Long
)
