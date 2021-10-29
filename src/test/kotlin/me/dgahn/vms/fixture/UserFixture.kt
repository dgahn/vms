package me.dgahn.vms.fixture

import me.dgahn.vms.domain.entity.User

fun getUser(totalVacations: Double = 15.0, usedVacations: Double = 0.0) = User(
    id = "test",
    password = "1234",
    totalVacations = totalVacations,
    usedVacations = usedVacations
)
