package com.playground.timereport.model

import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.persistence.Embeddable

@Embeddable
data class TimeRange(val from: LocalTime, val to: LocalTime) {

    fun getTimeInMinutes(): Long = ChronoUnit.MINUTES.between(from, to)
}