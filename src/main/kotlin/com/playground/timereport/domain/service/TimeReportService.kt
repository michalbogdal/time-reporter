package com.playground.timereport.domain.service

import com.playground.timereport.api.graphql.DayEventInput
import com.playground.timereport.domain.repository.TimeReportRepository
import com.playground.timereport.domain.model.DayEvent
import com.playground.timereport.domain.model.TimeRange
import com.playground.timereport.domain.model.TimeReport
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Component
class TimeReportService(
        private val timeReportRepository: TimeReportRepository,
        private val userService: UserService) {

    fun getTimeReport(year: Int, month: Int, onlyActiveDays: Boolean = true): TimeReport? {
        return userService.getLoggedUser().map { user ->
            var timeReport: TimeReport? = timeReportRepository.findByUserAndForYearAndForMonth(user,   year, month)
            if (timeReport != null && onlyActiveDays) {
                timeReport = TimeReport(user, timeReport.forYear, timeReport.forMonth, timeReport.events.asSequence().filter { dayEvent -> dayEvent.closeTime == null }.toMutableList())
            }
            timeReport
        }.orElse(null)
    }

    fun createDayReport(input: DayEventInput): DayEvent {
        return userService.getLoggedUser().map { user ->

            val date = LocalDate.parse(input.date)
            val workTime = TimeRange(from = LocalTime.parse(input.workTime.from), to = LocalTime.parse(input.workTime.to))
            val breakTime = TimeRange(from = LocalTime.parse(input.breakTime.from), to = LocalTime.parse(input.breakTime.to))
            val dayEvent = DayEvent(date = date, type = input.type, workTime = workTime, breakTime = breakTime)

            var timeReport = timeReportRepository.findByUserAndForYearAndForMonth(user, date.year, date.month.value)
            if (timeReport == null) {
                timeReport = TimeReport(user, date.year, date.month.value, mutableListOf())
            }

            val currentDayEvent = timeReport.events.find { de -> de.date == date }
            if (currentDayEvent?.closeTime == null) {
                timeReport.events.add(dayEvent)
                timeReportRepository.save(timeReport)
            }
            dayEvent
        }.orElseThrow { RuntimeException("user must be logged") }
    }

    fun updateDayEvent(input: DayEventInput): DayEvent? {
        return userService.getLoggedUser().map { user ->
            val date = LocalDate.parse(input.date)
            var dayEvent: DayEvent? = null
            var timeReport = timeReportRepository.findByUserAndForYearAndForMonth(user, date.year, date.month.value)
            if (timeReport != null) {

                val currentDayEvent = timeReport.events.find { dayEvent -> dayEvent.date == date }
                if (currentDayEvent != null) {
                    currentDayEvent.closeTime = LocalDateTime.now()

                    val workTime = TimeRange(from = LocalTime.parse(input.workTime.from), to = LocalTime.parse(input.workTime.to))
                    val breakTime = TimeRange(from = LocalTime.parse(input.breakTime.from), to = LocalTime.parse(input.breakTime.to))
                    dayEvent = DayEvent(date = date, type = input.type, workTime = workTime, breakTime = breakTime)
                    timeReport.events.add(dayEvent)
                    timeReportRepository.save(timeReport)
                }
            }
            dayEvent
        }.orElse(null)
    }

    fun removeDayEvent(year: Int, month: Int, day: Int): DayEvent? {
        return userService.getLoggedUser().map { user ->
            val date = LocalDate.of(year, month, day)
            var toDeleteEvent: DayEvent? = null
            var timeReport = timeReportRepository.findByUserAndForYearAndForMonth(user, year, month)
            if (timeReport != null) {
                val toDeleteEvent = timeReport.events.find { dayEvent -> dayEvent.date == date }
                if (toDeleteEvent != null && toDeleteEvent.closeTime == null) {
                    toDeleteEvent.closeTime = LocalDateTime.now()
                    timeReportRepository.save(timeReport)
                }
            }
            toDeleteEvent
        }.orElse(null)
    }
}