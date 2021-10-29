package me.dgahn.vms.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import me.dgahn.support.test.UnitTest
import me.dgahn.vms.domain.service.VacationService
import me.dgahn.vms.exception.custom.VacationNotFoundException
import me.dgahn.vms.exception.custom.VacationUsedException
import me.dgahn.vms.fixture.getNullEndAtCreateVacationRequest
import me.dgahn.vms.fixture.getNullIdVacations
import me.dgahn.vms.fixture.getUser
import me.dgahn.vms.fixture.getVacation
import me.dgahn.vms.fixture.getVacations
import me.dgahn.vms.infra.UserJpaRepository
import me.dgahn.vms.infra.VacationJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

@UnitTest
class VacationSimpleServiceUnitTest {
    @MockK
    lateinit var userJpaRepository: UserJpaRepository

    @MockK
    lateinit var vacationJpaRepository: VacationJpaRepository

    @MockK
    lateinit var vacationService: VacationService

    lateinit var vacationSimpleService: VacationSimpleService

    @BeforeEach
    fun setUp() {
        vacationSimpleService = VacationSimpleService(userJpaRepository, vacationJpaRepository, vacationService)
    }

    @Test
    fun `휴가를 만들 수 있다`() {
        val user = getUser()
        val nullIdVacations = getNullIdVacations(user)
        val savedVacations = getVacations(user)
        val request = getNullEndAtCreateVacationRequest()
        val expected = 9.0
        every { userJpaRepository.findById(user.id) } returns Optional.of(user)
        every { vacationService.create(request, user) } returns nullIdVacations
        every { vacationJpaRepository.saveAll(nullIdVacations) } returns savedVacations
        val actual = vacationSimpleService.create(request)
        actual.remainCount shouldBe expected
    }

    @Test
    fun `휴가를 찾지 못하면 VacationNotFoundException을 발생한다`() {
        val id = 1L
        every { vacationJpaRepository.findById(id) } returns Optional.empty()
        shouldThrow<VacationNotFoundException> { vacationSimpleService.findById(id) }
    }

    @Test
    fun `이미 지난 휴가를 취소하려고하면 VacationUsedException이 발생한다`() {
        val id = 1L
        val user = getUser()
        every { vacationJpaRepository.findById(id) } returns Optional.of(getVacation(user))
        shouldThrow<VacationUsedException> { vacationSimpleService.deleteById(id) }
    }

    @Test
    fun `존재하지 않는 휴가를 삭제하려고 하면 VacationNotFoundException이 발생한다`() {
        val id = 1L
        every { vacationJpaRepository.findById(id) } returns Optional.empty()
        shouldThrow<VacationNotFoundException> { vacationSimpleService.deleteById(id) }
    }
}
