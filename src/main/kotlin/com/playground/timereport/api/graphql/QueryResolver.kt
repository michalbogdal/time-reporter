package com.playground.timereport.api.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.playground.timereport.domain.model.TimeReport
import com.playground.timereport.domain.service.TimeReportService
import org.springframework.stereotype.Component

@Component
class Query(private val timeReportSerice: TimeReportService)
    : GraphQLQueryResolver {

    fun version() = "1.0.0"

    fun getTimeReport(forYear: Int, forMonth: Int, onlyActiveDays: Boolean = true): TimeReport? {
        return timeReportSerice.getTimeReport(forYear, forMonth, onlyActiveDays)
    }
}
