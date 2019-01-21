package com.playground.timereport.api.rest

import com.playground.timereport.api.graphql.DayEventInput
import com.playground.timereport.domain.model.DayEvent
import com.playground.timereport.domain.model.TimeReport
import com.playground.timereport.domain.service.TimeReportService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("app/v1/")
class TimeReportController(private val timeReportSerice: TimeReportService) {

    @GetMapping("/timereport/{year}/{month}")
    fun getTimeReport(@PathVariable year: Int, @PathVariable month: Int, @RequestParam onlyActiveDays: Boolean = true): TimeReport? {
        return timeReportSerice.getTimeReport(year, month, onlyActiveDays)
    }

    @PostMapping("/timereport")
    fun createDayReport(@RequestBody input: DayEventInput): DayEvent {
        return timeReportSerice.createDayReport(input)
    }

    @PutMapping("/timereport")
    fun updateDayEvent(@RequestBody input: DayEventInput): DayEvent? {
        return timeReportSerice.updateDayEvent(input)
    }

    @DeleteMapping("/timereport/{year}/{month}/{day}")
    fun removeDayEvent(@PathVariable year: Int, @PathVariable month: Int, @PathVariable day: Int): DayEvent? {
        return timeReportSerice.removeDayEvent(year, month, day)
    }
}