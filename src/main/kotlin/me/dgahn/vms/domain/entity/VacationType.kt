package me.dgahn.vms.domain.entity

@Suppress("MagicNumber")
enum class VacationType(val dayUnit: Double) {
    ANNUAL_LEAVE(1.0),
    HALF_LEAVE(0.5),
    HALF_HALF_LEAVE(0.25);
}
