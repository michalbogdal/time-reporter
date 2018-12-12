package com.playground.timereport.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.playground.timereport.dao.TimeReportDao
import com.playground.timereport.dao.UserDAO
import com.playground.timereport.model.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class TimeReportMutationResolver(
        private val timeReportDao: TimeReportDao
) : GraphQLMutationResolver {

    fun createDayEvent(input: DayEventInput): DayEvent {
        val date = LocalDate.parse(input.date)
        val workTime = TimeRange(from = LocalTime.parse(input.workTime.from), to = LocalTime.parse(input.workTime.to))
        val breakTime = TimeRange(from = LocalTime.parse(input.breakTime.from), to = LocalTime.parse(input.breakTime.to))
        val dayEvent = DayEvent(date = date, type = input.type, workTime = workTime, breakTime = breakTime)

        var timeReport = timeReportDao.findByForYearAndForMonth(date.year, date.month.value)
        if (timeReport == null) {
            timeReport = TimeReport(date.year, date.month.value, mutableListOf())
        }

        val currentDayEvent = timeReport.events.find { de -> de.date == date }
        if (currentDayEvent?.closeTime == null) {
            timeReport.events.add(dayEvent)
            timeReportDao.save(timeReport)
        }

        return dayEvent
    }


    fun updateDayEvent(input: DayEventInput): DayEvent? {
        val date = LocalDate.parse(input.date)
        var timeReport = timeReportDao.findByForYearAndForMonth(date.year, date.month.value)
        if (timeReport != null) {

            val currentDayEvent = timeReport.events.find { dayEvent -> dayEvent.date == date }
            if (currentDayEvent != null) {
                currentDayEvent.closeTime = LocalDateTime.now()

                val workTime = TimeRange(from = LocalTime.parse(input.workTime.from), to = LocalTime.parse(input.workTime.to))
                val breakTime = TimeRange(from = LocalTime.parse(input.breakTime.from), to = LocalTime.parse(input.breakTime.to))
                val dayEvent = DayEvent(date = date, type = input.type, workTime = workTime, breakTime = breakTime)
                timeReport.events.add(dayEvent)
                timeReportDao.save(timeReport)
            }
        }

        return null
    }

    fun removeDayEvent(date: String): DayEvent? {
        val date = LocalDate.parse(date)
        var timeReport = timeReportDao.findByForYearAndForMonth(date.year, date.month.value)
        if (timeReport != null) {
            val toDeleteEvent = timeReport.events.find { dayEvent -> dayEvent.date == date }
            if (toDeleteEvent != null && toDeleteEvent.closeTime == null) {
                toDeleteEvent.closeTime = LocalDateTime.now()
                timeReportDao.save(timeReport)
                return toDeleteEvent
            }
        }
        return null
    }
}

data class DayEventInput(val date: String, val type: DayEventType, val workTime: TimeRangeInput, val breakTime: TimeRangeInput)

data class TimeRangeInput(val from: String, val to: String)