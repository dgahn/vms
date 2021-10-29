package me.dgahn.vms.domain.service

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import me.dgahn.vms.domain.entity.User
import me.dgahn.vms.domain.entity.Vacation
import me.dgahn.vms.domain.entity.VacationType
import me.dgahn.vms.dto.CreateVacationRequest
import me.dgahn.vms.ex.minus
import me.dgahn.vms.exception.custom.VacationRequestNotEnoughException
import org.springframework.stereotype.Service
import java.time.DayOfWeek

private val WEEKEND = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

@Service
class VacationService {
    fun create(request: CreateVacationRequest, user: User): List<Vacation> = when {
        request.useCount != null && request.type != null -> createByUseCount(request.useCount, request, user)
        request.endAt != null -> createByPeriod(request, user)
        else -> throw VacationRequestNotEnoughException()
    }

    private fun createByPeriod(request: CreateVacationRequest, user: User): List<Vacation> {
        val endAt = request.endAt!!
        val startAt = request.startAt
        val maxCount = endAt - startAt
        var currentCount = 0
        val vacations = mutableListOf<Vacation>()
        while (vacations.size < maxCount) {
            vacations.addVacation(startAt, request, currentCount, user)
            currentCount++
        }
        return vacations.toList()
    }

    private fun MutableList<Vacation>.addVacation(
        startAt: LocalDate,
        request: CreateVacationRequest,
        currentCount: Int,
        user: User
    ) {
        val date = startAt.toJavaLocalDate().plusDays(currentCount.toLong())
        if (!WEEKEND.contains(date.dayOfWeek)) {
            this.add(
                Vacation(
                    type = VacationType.ANNUAL_LEAVE,
                    comment = request.comment,
                    date = date,
                    user = user
                )
            )
        }
    }

    private fun createByUseCount(
        useCount: Int,
        request: CreateVacationRequest,
        user: User
    ) = (1..useCount).map {
        Vacation(
            type = request.type ?: VacationType.ANNUAL_LEAVE,
            comment = request.comment,
            date = request.startAt.toJavaLocalDate(),
            user = user
        )
    }
}
