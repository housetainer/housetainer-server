package com.housetainer.domain.model.schedule

import com.housetainer.domain.entity.schedule.AccessType
import com.housetainer.domain.entity.schedule.ScheduleType

data class CreateScheduleRequest(
    val title: String,
    val content: String,
    val date: Long,
    val category: String,
    val type: ScheduleType = ScheduleType.NORMAL,
    val accessType: AccessType = AccessType.PUBLIC,
    val visible: Boolean = true,
    val photo: String? = null,
    val relatedLink: String? = null,
    val parentScheduleId: String? = null
)
