package com.playground.timereport.domain.repository

import com.playground.timereport.domain.model.TimeReport
import com.playground.timereport.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TimeReportRepository : JpaRepository<TimeReport, Int> {

    fun findByUsernameAndForYearAndForMonth(username: String, year: Int, month: Int): TimeReport?
}