package me.dgahn.vms.application

import me.dgahn.vms.domain.entity.Vacation
import me.dgahn.vms.domain.service.VacationService
import me.dgahn.vms.dto.CreateVacationRequest
import me.dgahn.vms.dto.CreateVacationResponse
import me.dgahn.vms.exception.custom.UserNotFoundException
import me.dgahn.vms.exception.custom.VacationNotFoundException
import me.dgahn.vms.exception.custom.VacationUsedException
import me.dgahn.vms.infra.UserJpaRepository
import me.dgahn.vms.infra.VacationJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class VacationSimpleService(
    private val userJpaRepository: UserJpaRepository,
    private val vacationJpaRepository: VacationJpaRepository,
    private val vacationService: VacationService
) {
    @Transactional
    fun create(request: CreateVacationRequest): CreateVacationResponse {
        val user = userJpaRepository.findById(request.userId).orElseThrow { throw UserNotFoundException() }
        val vacations = vacationJpaRepository.saveAll(vacationService.create(request, user))
        user.updateUsedVacations(vacations.sumOf { it.type.dayUnit })
        return CreateVacationResponse(vacations.map { it.id }, user.remainVacations, user.totalVacations)
    }

    fun findAllBy(userId: String, offsetVacationId: Long, limit: Int): List<Vacation> =
        vacationJpaRepository.findAllBy(userId, offsetVacationId, PageRequest.ofSize(limit))

    fun findById(id: Long): Vacation = vacationJpaRepository.findById(id)
        .orElseThrow { throw VacationNotFoundException() }

    @Transactional
    fun deleteById(id: Long) {
        val vacation = vacationJpaRepository.findById(id).orElseThrow {
            throw VacationNotFoundException()
        }
        if (vacation.isUsed) {
            throw VacationUsedException()
        }
        vacation.user.updateUsedVacations(-vacation.type.dayUnit)
        vacationJpaRepository.deleteById(id)
    }
}
