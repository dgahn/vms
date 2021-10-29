package me.dgahn.vms.fixture

import me.dgahn.vms.domain.entity.User
import me.dgahn.vms.domain.entity.Vacation
import me.dgahn.vms.domain.entity.VacationType
import me.dgahn.vms.dto.CreateVacationRequest
import me.dgahn.vms.dto.CreateVacationResponse
import me.dgahn.vms.dto.VacationListResponse
import me.dgahn.vms.dto.VacationResponse
import me.dgahn.vms.ex.toKLocalDate
import java.time.LocalDate

fun getCreateVacationRequest() = CreateVacationRequest(
    userId = "test",
    type = VacationType.ANNUAL_LEAVE,
    useCount = 1,
    comment = "연차 사용",
    startAt = getPlusDay(1).toKLocalDate(),
    endAt = getPlusDay(2).toKLocalDate()
)

fun getNullEndAtCreateVacationRequest(
    type: VacationType = VacationType.ANNUAL_LEAVE,
    useCount: Int = 1,
    start: Int = 1
) = CreateVacationRequest(
    userId = "test",
    type = type,
    useCount = useCount,
    comment = "연차 사용",
    startAt = getPlusDay(start.toLong()).toKLocalDate(),
)

fun getNullTypeCreateVacationRequest(
    start: Int = 1,
    end: Int = 1
) = CreateVacationRequest(
    userId = "test",
    comment = "연차 사용",
    startAt = getPlusDay(start.toLong()).toKLocalDate(),
    endAt = getPlusDay(end.toLong()).toKLocalDate()
)

fun getNotEnoughCreateVacationRequest() = CreateVacationRequest(
    userId = "test",
    comment = "",
    startAt = getPlusDay(1).toKLocalDate(),
)

fun getCreateVacationResponse(ids: List<Long> = listOf(1), remainCount: Double = 14.0) = CreateVacationResponse(
    ids = ids,
    remainCount = remainCount,
    totalCount = 15.0
)

fun getNullIdVacations(user: User): List<Vacation> = (1..5).map {
    Vacation(
        type = VacationType.ANNUAL_LEAVE,
        comment = "개인 사유",
        date = LocalDate.now(),
        user = user
    )
}

fun getVacation(user: User): Vacation = Vacation(
    id = 1,
    type = VacationType.ANNUAL_LEAVE,
    comment = "개인 사유",
    date = LocalDate.now(),
    user = user
)

fun getVacations(user: User, offsetVacationId: Int = 1, limit: Int = 5): List<Vacation> =
    (offsetVacationId..offsetVacationId + limit).map {
        Vacation(
            type = VacationType.ANNUAL_LEAVE,
            comment = "개인 사유",
            date = LocalDate.now(),
            user = user
        )
    }

fun getVacationListResponse(vacations: List<VacationResponse>) = VacationListResponse(
    vacations = vacations
)

fun getVacationResponseList(start: Int = 1, end: Int = 10) = (start..(start + end)).map {
    VacationResponse(
        id = it.toLong(),
        type = VacationType.ANNUAL_LEAVE,
        comment = "개인 사유",
        date = LocalDate.now().toKLocalDate()
    )
}
