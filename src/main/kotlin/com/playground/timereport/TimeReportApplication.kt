package com.playground.timereport

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class TimeReportApplication {

   companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(TimeReportApplication::class.java, *args)
        }
    }
}

