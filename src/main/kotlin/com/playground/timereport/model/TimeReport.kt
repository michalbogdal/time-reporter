package com.playground.timereport.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "time_report")
data class TimeReport(

        @Column(name = "for_year", nullable = false)
        val forYear: Int,

        @Column(name = "for_month", nullable = false)
        val forMonth: Int,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
        @OrderBy(value = "date")
        val events: MutableList<DayEvent>

) {

    companion object {
        const val WORK_HOURS_PER_DAY = 8
        const val WORKING_DAYS_IN_WEEK = 5
        const val NUM_DAYS_IN_WEEK = 7
    }


    @Id
    @GeneratedValue
    val id: Int = 0

    fun getReportedHours(): Double = events.fold(0.0) { acc, dayEvent -> acc + dayEvent.getReportedHours() }

    fun getExpectedHours(): Int = LocalDate.of(forYear, forMonth, 1).lengthOfMonth() / NUM_DAYS_IN_WEEK * WORKING_DAYS_IN_WEEK * WORK_HOURS_PER_DAY
}