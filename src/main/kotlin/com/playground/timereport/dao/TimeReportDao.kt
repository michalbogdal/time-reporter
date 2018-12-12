package com.playground.timereport.dao

import com.playground.timereport.model.TimeReport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TimeReportDao : JpaRepository<TimeReport, Int> {

    fun findByForYearAndForMonth(year: Int, month: Int): TimeReport?
}