package com.playground.timereport.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "day_event")
data class DayEvent(

        @Column(name = "date")
        val date: LocalDate,

        @Column(name = "type")
        @Enumerated(EnumType.STRING)
        val type: DayEventType,

        @AttributeOverrides( value = [
            AttributeOverride(name="from", column = Column(name="work_from")),
            AttributeOverride(name="to", column = Column(name="work_to"))
        ])
        val workTime: TimeRange,

        @AttributeOverrides( value = [
            AttributeOverride(name="from", column = Column(name="break_from")),
            AttributeOverride(name="to", column = Column(name="break_to"))
        ])
        val breakTime: TimeRange
){

    @Id
    @GeneratedValue
    val id: Int = 0

    @Column(name = "create_time")
    val createTime: LocalDateTime = LocalDateTime.now()

    @Column(name = "close_time")
    var closeTime: LocalDateTime? = null

    fun getReportedHours(): Double {
        return (workTime.getTimeInMinutes() - breakTime.getTimeInMinutes()) / 60.0
    }

    fun getTextRepresentation(): String{
        return "$date, $type, ${workTime.from}, ${workTime.to}, ${breakTime.from}, ${breakTime.to}";
    }
}



