package me.dgahn.vms.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import me.dgahn.vms.domain.entity.Vacation
import me.dgahn.vms.domain.entity.VacationType
import me.dgahn.vms.ex.toKLocalDate

@Serializable
data class CreateVacationRequest(
    val userId: String,
    val type: VacationType? = null,
    val useCount: Int? = null,
    val comment: String,
    val startAt: LocalDate,
    val endAt: LocalDate? = null
)

@Serializable
data class CreateVacationResponse(
    val ids: List<Long>,
    val remainCount: Double,
    val totalCount: Double
)

@Serializable
data class VacationResponse(
    val id: Long,
    val type: VacationType,
    val comment: String,
    val date: LocalDate
) {
    constructor(vacation: Vacation) : this(
        id = vacation.id,
        type = vacation.type,
        comment = vacation.comment,
        date = vacation.date.toKLocalDate()
    )
}

@Serializable
data class VacationListResponse(
    val vacations: List<VacationResponse>
)

@Serializable
data class DeleteVacationResponse(
    val id: Long
)
