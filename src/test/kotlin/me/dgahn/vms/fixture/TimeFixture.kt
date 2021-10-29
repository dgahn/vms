package me.dgahn.vms.fixture

import java.time.LocalDate

fun getPlusDay(day: Long = 1): LocalDate = LocalDate.now().plusDays(day)
