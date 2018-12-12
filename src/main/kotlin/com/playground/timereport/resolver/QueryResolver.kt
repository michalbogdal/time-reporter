package com.playground.timereport.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.playground.timereport.dao.TimeReportDao
import com.playground.timereport.model.TimeReport
import org.springframework.stereotype.Component

@Component
class Query(private val timeReportDao: TimeReportDao) : GraphQLQueryResolver {

    fun version() = "1.0.0"

    fun getTimeReport(forYear: Int, forMonth: Int, onlyActiveDays: Boolean = true): TimeReport? {

        val timeReport: TimeReport? = timeReportDao.findByForYearAndForMonth(forYear, forMonth)
        return if (timeReport == null || !onlyActiveDays)
            timeReport
        else TimeReport(timeReport.forYear, timeReport.forMonth, timeReport.events.asSequence().filter { dayEvent -> dayEvent.closeTime == null }.toMutableList())
    }
}
