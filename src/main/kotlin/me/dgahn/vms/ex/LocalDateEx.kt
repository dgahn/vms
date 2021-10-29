package me.dgahn.vms.ex

import kotlinx.datetime.Month
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.datetime.LocalDate as KLocalDate
import kotlinx.datetime.LocalDateTime as KLocalDateTime

private const val PERIOD_CONST = 1

fun LocalDate.toKLocalDate() = KLocalDate(
    year = this.year,
    month = Month(this.month.value),
    dayOfMonth = this.dayOfMonth
)

operator fun LocalDate.minus(localDate: LocalDate): Int =
    (this.toEpochDay() - localDate.toEpochDay()).toInt() + PERIOD_CONST

operator fun KLocalDate.minus(localDate: KLocalDate): Int = (this.toJavaLocalDate() - localDate.toJavaLocalDate())

fun LocalDateTime.toKLocalDateTime() = KLocalDateTime(
    year = this.year,
    month = Month(this.month.value),
    dayOfMonth = this.dayOfMonth,
    hour = this.hour,
    minute = this.minute,
    second = this.second,
    nanosecond = this.nano
)
