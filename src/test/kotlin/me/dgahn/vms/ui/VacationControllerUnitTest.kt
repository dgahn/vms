package me.dgahn.vms.ui

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.dgahn.vms.application.VacationSimpleService
import me.dgahn.vms.domain.entity.VacationType
import me.dgahn.vms.exception.custom.UserVacationsUpdateException
import me.dgahn.vms.exception.custom.VacationNotFoundException
import me.dgahn.vms.exception.custom.VacationRequestNotEnoughException
import me.dgahn.vms.exception.custom.VacationUsedException
import me.dgahn.vms.fixture.getCreateVacationRequest
import me.dgahn.vms.fixture.getCreateVacationResponse
import me.dgahn.vms.fixture.getNotEnoughCreateVacationRequest
import me.dgahn.vms.fixture.getUser
import me.dgahn.vms.fixture.getVacations
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(controllers = [VacationController::class])
class VacationControllerUnitTest : RestControllerTest() {
    @MockkBean
    lateinit var vacationSimpleService: VacationSimpleService

    @Test
    fun `휴가를 신청할 수 있다`() {
        val request = getCreateVacationRequest()
        val response = getCreateVacationResponse()
        every { vacationSimpleService.create(request) } returns response
        mockMvc.perform(
            post("/api/vacations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(request))
        )
            .andExpect(
                status().isCreated,
            )
            .andDo(
                document(
                    "vacation-create-created",
                    requestFields(
                        fieldWithPath("userId").description("사용자 식별자").type(JsonFieldType.STRING),
                        fieldWithPath("type").description("휴가의 종류").type(VacationType::class),
                        fieldWithPath("useCount").description("휴가의 식별자").type(JsonFieldType.NUMBER),
                        fieldWithPath("comment").description("휴가에 대한 코멘트").type(JsonFieldType.STRING),
                        fieldWithPath("startAt").description("휴가 시작 날짜").type(LocalDateTime::class),
                        fieldWithPath("endAt").description("휴가 끝 날짜").type(LocalDateTime::class)
                    ),
                    responseFields(
                        fieldWithPath("ids").description("요청으로 인해 생성된 휴가들의 식별자 목록").type(JsonFieldType.ARRAY),
                        fieldWithPath("remainCount").description("남은 휴가 일수").type(JsonFieldType.NUMBER),
                        fieldWithPath("totalCount").description("총 휴가 일수").type(JsonFieldType.NUMBER)
                    )
                )
            )
    }

    @Test
    fun `휴가를 신청하는데 가진 휴가보다 많이 쓰려고 하면 BadRequest를 반환한다`() {
        val request = getCreateVacationRequest()
        every { vacationSimpleService.create(request) } throws UserVacationsUpdateException()
        mockMvc.perform(
            post("/api/vacations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(request))
        )
            .andExpect(
                status().isBadRequest,
            )
            .andDo(
                document(
                    "vacation-create-bad-request",
                    requestFields(
                        fieldWithPath("userId").description("사용자 식별자").type(JsonFieldType.STRING),
                        fieldWithPath("type").description("휴가의 종류").type(VacationType::class),
                        fieldWithPath("useCount").description("휴가의 식별자").type(JsonFieldType.NUMBER),
                        fieldWithPath("comment").description("휴가에 대한 코멘트").type(JsonFieldType.STRING),
                        fieldWithPath("startAt").description("휴가 시작 날짜").type(LocalDateTime::class),
                        fieldWithPath("endAt").description("휴가 끝 날짜").type(LocalDateTime::class)
                    ),
                    responseFields(
                        fieldWithPath("code").description("에러 코드").type(JsonFieldType.STRING),
                        fieldWithPath("message").description("에러에 대한 메시지").type(JsonFieldType.STRING),
                        fieldWithPath("trace").description("에러 발생에 따른 stacktrace").type(JsonFieldType.STRING),
                        fieldWithPath("timestamp").description("에러 발생한 시간").type(JsonFieldType.STRING),
                    )
                )
            )
    }

    @Test
    fun `휴가를 신청하는데 프로퍼티를 충분히 넣지 않으면 BadRequest를 반환한다`() {
        val request = getNotEnoughCreateVacationRequest()
        every { vacationSimpleService.create(request) } throws VacationRequestNotEnoughException()
        mockMvc.perform(
            post("/api/vacations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(request))
        )
            .andExpect(
                status().isBadRequest,
            )
            .andDo(
                document(
                    "vacation-create-not-enough-bad-request",
                    requestFields(
                        fieldWithPath("userId").description("사용자 식별자").type(JsonFieldType.STRING),
                        fieldWithPath("comment").description("휴가에 대한 코멘트").type(JsonFieldType.STRING),
                        fieldWithPath("startAt").description("휴가 시작 날짜").type(LocalDateTime::class),
                    ),
                    responseFields(
                        fieldWithPath("code").description("에러 코드").type(JsonFieldType.STRING),
                        fieldWithPath("message").description("에러에 대한 메시지").type(JsonFieldType.STRING),
                        fieldWithPath("trace").description("에러 발생에 따른 stacktrace").type(JsonFieldType.STRING),
                        fieldWithPath("timestamp").description("에러 발생한 시간").type(JsonFieldType.STRING),
                    )
                )
            )
    }

    @Test
    fun `휴가 목록을 조회할 수 있다`() {
        val user = getUser()
        val offsetVacationId = 10
        val limit = 5
        val vacations = getVacations(user, offsetVacationId)
        every { vacationSimpleService.findAllBy(user.id, offsetVacationId.toLong(), limit) } returns vacations
        mockMvc.perform(
            get(
                "/api/vacations?user-id={user-id}&offset-vacation-id={vacation-id}&limit={limit}",
                user.id,
                offsetVacationId,
                limit
            ).accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "vacation-list-ok",
                    requestParameters(
                        parameterWithName("user-id").description("휴가를 사용한 사람의 아이디"),
                        parameterWithName("offset-vacation-id").description("조회하려는 휴가 목록의 최소 offset 값으로 해당 값은 포함되지 않음"),
                        parameterWithName("limit").description("조회하려는 휴가 목록의 갯수"),
                    ),
                    responseFields(
                        fieldWithPath("vacations").description("휴가 목록").type(JsonFieldType.ARRAY),
                        fieldWithPath("vacations[].id").description("휴가 식별자").type(JsonFieldType.NUMBER),
                        fieldWithPath("vacations[].type").description("휴가 타입").type(VacationType::class),
                        fieldWithPath("vacations[].comment").description("휴가 코멘트").type(JsonFieldType.STRING),
                        fieldWithPath("vacations[].date").description("휴가 날짜").type(JsonFieldType.STRING),
                    )
                )
            )
    }

    @Test
    fun `휴가를 조회할 수 있다`() {
        val user = getUser()
        val vacationId = 1L
        every { vacationSimpleService.findById(vacationId) } returns getVacations(user).first()
        mockMvc.perform(get("/api/vacations/{vacation-id}", vacationId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "vacation-get-ok",
                    pathParameters(
                        parameterWithName("vacation-id").description("휴가 식별자")
                    ),
                    responseFields(
                        fieldWithPath("id").description("휴가 식별자").type(JsonFieldType.NUMBER),
                        fieldWithPath("type").description("휴가 타입").type(VacationType::class),
                        fieldWithPath("comment").description("휴가 코멘트").type(JsonFieldType.STRING),
                        fieldWithPath("date").description("휴가 날짜").type(JsonFieldType.STRING),
                    )
                )
            )
    }

    @Test
    fun `휴가를 찾지 못하면 BadRequest를 반환한다`() {
        val vacationId = 1L
        every { vacationSimpleService.findById(vacationId) } throws VacationNotFoundException()
        mockMvc.perform(get("/api/vacations/{vacation-id}", vacationId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andDo(
                document(
                    "vacation-get-id-bad-request",
                    pathParameters(
                        parameterWithName("vacation-id").description("휴가 식별자")
                    ),
                    responseFields(
                        fieldWithPath("code").description("에러 코드").type(JsonFieldType.STRING),
                        fieldWithPath("message").description("에러에 대한 메시지").type(JsonFieldType.STRING),
                        fieldWithPath("trace").description("에러 발생에 따른 stacktrace").type(JsonFieldType.STRING),
                        fieldWithPath("timestamp").description("에러 발생한 시간").type(JsonFieldType.STRING),
                    )
                )
            )
    }

    @Test
    fun `휴가를 취소할 수 있다`() {
        val vacationId = 1L
        every { vacationSimpleService.deleteById(vacationId) } just runs
        mockMvc.perform(delete("/api/vacations/{vacation-id}", vacationId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "vacation-delete-ok",
                    pathParameters(
                        parameterWithName("vacation-id").description("휴가를 사용한 사람의 아이디")
                    ),
                    responseFields(
                        fieldWithPath("id").description("휴가 식별자").type(JsonFieldType.NUMBER)
                    )
                )
            )
    }

    @Test
    fun `취소하려는 휴가의 날짜가 오늘 포함 이전이면 BadRequest를 반환한다`() {
        val vacationId = 1L
        every { vacationSimpleService.deleteById(vacationId) } throws VacationUsedException()
        mockMvc.perform(delete("/api/vacations/{vacation-id}", vacationId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andDo(
                document(
                    "vacation-delete-used-bad-request",
                    pathParameters(
                        parameterWithName("vacation-id").description("휴가 식별자")
                    ),
                    responseFields(
                        fieldWithPath("code").description("에러 코드").type(JsonFieldType.STRING),
                        fieldWithPath("message").description("에러에 대한 메시지").type(JsonFieldType.STRING),
                        fieldWithPath("trace").description("에러 발생에 따른 stacktrace").type(JsonFieldType.STRING),
                        fieldWithPath("timestamp").description("에러 발생한 시간").type(JsonFieldType.STRING),
                    )
                )
            )
    }
}
