package me.dgahn.vms.ui

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.dgahn.support.test.IntegrationTest
import me.dgahn.vms.domain.entity.User
import me.dgahn.vms.domain.entity.Vacation
import me.dgahn.vms.dto.DeleteVacationResponse
import me.dgahn.vms.dto.VacationListResponse
import me.dgahn.vms.dto.VacationResponse
import me.dgahn.vms.fixture.getCreateVacationRequest
import me.dgahn.vms.fixture.getCreateVacationResponse
import me.dgahn.vms.fixture.getUser
import me.dgahn.vms.fixture.getVacations
import me.dgahn.vms.infra.UserJpaRepository
import me.dgahn.vms.infra.VacationJpaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@IntegrationTest
class VacationControllerIntegrationTest(
    @Autowired
    val userJpaRepository: UserJpaRepository,
    @Autowired
    val vacationJpaRepository: VacationJpaRepository
) : RestControllerTest() {
    lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = userJpaRepository.save(getUser())
    }

    @AfterEach
    fun cleanUp() {
        vacationJpaRepository.deleteAll()
        userJpaRepository.deleteAll()
    }

    @Nested
    inner class Create {
        @Test
        fun `연차를 신청할 수 있다`() {
            val request = getCreateVacationRequest()
            val response = getCreateVacationResponse()
            mockMvc.perform(
                post("/api/vacations")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Json.encodeToString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(content().string(Json.encodeToString(response)))
        }
    }

    @Nested
    inner class Find {
        lateinit var vacations: List<Vacation>

        @BeforeEach
        fun setUp() {
            vacations = vacationJpaRepository.saveAll(getVacations(user = user, limit = 10))
        }

        @Test
        fun `휴가 목록을 조회할 수 있다`() {
            val offsetVacationId = vacations.first().id + 5
            val limit = 5
            val vacationResponses = vacations.map { VacationResponse(it) }.subList(6, 11)
            val vacationListResponse = VacationListResponse(vacationResponses)
            mockMvc.perform(
                get(
                    "/api/vacations?user-id={user-id}&offset-vacation-id={vacation-id}&limit={limit}",
                    user.id,
                    offsetVacationId,
                    limit
                ).accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk)
                .andExpect(content().string(Json.encodeToString(vacationListResponse)))
        }

        @Test
        fun `휴가를 조회할 수 있다`() {
            val vacationResponse = VacationResponse(vacations.first())
            val id = vacationResponse.id
            mockMvc.perform(get("/api/vacations/{vacation-id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().string(Json.encodeToString(vacationResponse)))
        }
    }

    @Nested
    inner class Delete {
        lateinit var vacations: List<Vacation>

        @BeforeEach
        fun setUp() {
            vacations = vacationJpaRepository.saveAll(getVacations(user = user, limit = 10))
            user.updateUsedVacations(vacations.sumOf { it.type.dayUnit })
            userJpaRepository.saveAndFlush(user)
        }

        @Test
        fun `휴가를 취소할 수 있다`() {
            val now = LocalDate.now()
            mockkStatic(LocalDate::class)
            every { LocalDate.now() } returns now.minusDays(1)
            val vacationResponse = DeleteVacationResponse(vacations.first().id)
            val id = vacationResponse.id
            mockMvc.perform(delete("/api/vacations/{vacation-id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().string(Json.encodeToString(vacationResponse)))
            vacationJpaRepository.count() shouldBe vacations.size - 1
            unmockkStatic(LocalDate::class)
        }
    }
}
