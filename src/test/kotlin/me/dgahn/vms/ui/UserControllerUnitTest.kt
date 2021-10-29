package me.dgahn.vms.ui

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.dgahn.vms.application.UserSimpleService
import me.dgahn.vms.dto.SignInRequest
import me.dgahn.vms.exception.custom.PasswordInvalidException
import me.dgahn.vms.exception.custom.UserNotFoundException
import me.dgahn.vms.fixture.getUser
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [UserController::class])
class UserControllerUnitTest : RestControllerTest() {
    @MockkBean
    lateinit var userSimpleService: UserSimpleService

    @Test
    fun `사용자는 로그인할 수 있다`() {
        val user = getUser()
        every { userSimpleService.signIn(user.id, user.password) } returns user.id
        mockMvc.perform(
            post("/api/users/sign-in")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(SignInRequest(user.id, user.password)))
        )
            .andExpect(
                status().isOk,
            )
            .andDo(
                document(
                    "users-sign-in-ok",
                    requestFields(
                        fieldWithPath("id").description("로그인할 사용자의 아이디").type(JsonFieldType.STRING),
                        fieldWithPath("password").description("로그인할 사용자의 비밀번호").type(JsonFieldType.STRING),
                    ),
                    responseFields(
                        fieldWithPath("id").description("로그인한 사용자의 아이디").type(JsonFieldType.STRING)
                    )
                )
            )
    }

    @Test
    fun `로그인시 아이디가 존재하지 않으면 BadRequest를 받는다`() {
        val user = getUser()
        every { userSimpleService.signIn(user.id, user.password) } throws UserNotFoundException()
        mockMvc.perform(
            post("/api/users/sign-in")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(SignInRequest(user.id, user.password)))
        )
            .andExpect(
                status().isBadRequest,
            )
            .andDo(
                document(
                    "users-sign-in-id-bad-request",
                    requestFields(
                        fieldWithPath("id").description("로그인할 사용자의 아이디").type(JsonFieldType.STRING),
                        fieldWithPath("password").description("로그인할 사용자의 비밀번호").type(JsonFieldType.STRING),
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
    fun `로그인시 비밀번호가 틀리면 BadRequest를 받는다`() {
        val user = getUser()
        every { userSimpleService.signIn(user.id, user.password) } throws PasswordInvalidException()
        mockMvc.perform(
            post("/api/users/sign-in")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(SignInRequest(user.id, user.password)))
        )
            .andExpect(
                status().isBadRequest,
            )
            .andDo(
                document(
                    "users-sign-in-password-bad-request",
                    requestFields(
                        fieldWithPath("id").description("로그인할 사용자의 아이디").type(JsonFieldType.STRING),
                        fieldWithPath("password").description("로그인할 사용자의 비밀번호").type(JsonFieldType.STRING),
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
