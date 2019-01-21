package com.playground.timereport.api.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.playground.timereport.domain.model.DayEvent
import com.playground.timereport.domain.model.DayEventType
import com.playground.timereport.domain.service.TimeReportService
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class TimeReportMutationResolver(
        private val timeReportSerice: TimeReportService
) : GraphQLMutationResolver {

    fun createDayEvent(input: DayEventInput): DayEvent {
        return timeReportSerice.createDayReport(input)
    }


    fun updateDayEvent(input: DayEventInput): DayEvent? {
        return timeReportSerice.updateDayEvent(input)
    }

    fun removeDayEvent(date: String): DayEvent? {
        val date = LocalDate.parse(date)
        return timeReportSerice.removeDayEvent(date.year, date.month.value, date.dayOfMonth)
    }
}


data class DayEventInput(val date: String, val type: DayEventType, val workTime: TimeRangeInput, val breakTime: TimeRangeInput)

data class TimeRangeInput(val from: String, val to: String)