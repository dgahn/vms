package me.dgahn.vms.domain.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.datetime.toJavaLocalDate
import me.dgahn.support.test.UnitTest
import me.dgahn.vms.domain.entity.User
import me.dgahn.vms.domain.entity.Vacation
import me.dgahn.vms.domain.entity.VacationType
import me.dgahn.vms.dto.CreateVacationRequest
import me.dgahn.vms.exception.custom.VacationRequestNotEnoughException
import me.dgahn.vms.fixture.getNotEnoughCreateVacationRequest
import me.dgahn.vms.fixture.getNullEndAtCreateVacationRequest
import me.dgahn.vms.fixture.getNullTypeCreateVacationRequest
import me.dgahn.vms.fixture.getUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate

@UnitTest
internal class VacationServiceUnitTest {
    private val vacationService = VacationService()

    @ParameterizedTest
    @CsvSource(value = ["ANNUAL_LEAVE:4:1", "HALF_LEAVE:3:1", "HALF_HALF_LEAVE:4:2"], delimiter = ':')
    fun `휴가타입과 사용일수, 시작시간으로 휴가 목록을 생성할 수 있다`(vacationType: VacationType, useCount: Int, start: Int) {
        val user = getUser(15.0, 10.0)
        val request = getNullEndAtCreateVacationRequest(vacationType, useCount, start)
        val actual = vacationService.create(request, user)

        val expected = createVacation(useCount, vacationType, request, user)

        actual shouldBe expected
    }

    @ParameterizedTest
    @CsvSource(value = ["1:1:2", "1:3:2", "1:4:2"], delimiter = ':')
    fun `휴가 타입이 존재하지 않으면 시작시간과 끝시간으로 휴가 목록을 생성할 수 있다`(start: Int, end: Int, plusDay: Long) {
        mockkStatic(LocalDate::class)
        every { LocalDate.now() } returns LocalDate.of(2021, 10, 22)
        val user = getUser(15.0, 10.0)
        val request = getNullTypeCreateVacationRequest(start, end)
        val actual = vacationService.create(request, user)

        val useCount = end - start + 1
        val expected = createVacation(useCount, VacationType.ANNUAL_LEAVE, request, user, plusDay)
        actual.forEachIndexed { index, vacation ->
            vacation.date shouldBe expected[index].date
        }
        actual.size shouldBe expected.size
        unmockkStatic(LocalDate::class)
    }

    @Test
    fun `휴가 요청에 사용 일수, 타입, 끝나는시간을 모두 입력하지 않으면 VacationRequestNotEnoughException이 발생한다`() {
        val user = getUser(15.0, 10.0)
        val request = getNotEnoughCreateVacationRequest()
        shouldThrow<VacationRequestNotEnoughException> { vacationService.create(request, user) }
    }

    private fun createVacation(
        useCount: Int,
        vacationType: VacationType,
        request: CreateVacationRequest,
        user: User,
        plusDay: Long = 0
    ) = (1..useCount).map {
        Vacation(
            type = vacationType,
            comment = request.comment,
            date = request.startAt.toJavaLocalDate().plusDays(plusDay + it - 1),
            user = user
        )
    }
}
