package me.dgahn.vms.ui

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.dgahn.support.test.IntegrationTest
import me.dgahn.vms.dto.SignInRequest
import me.dgahn.vms.dto.SignInResponse
import me.dgahn.vms.fixture.getUser
import me.dgahn.vms.infra.UserJpaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@IntegrationTest
class UserControllerIntegrationTest(
    @Autowired
    val userJpaRepository: UserJpaRepository
) : RestControllerTest() {

    @BeforeEach
    fun setUp() {
        userJpaRepository.save(getUser())
    }

    @AfterEach
    fun cleanUp() {
        userJpaRepository.deleteAll()
    }

    @Test
    fun `사용자는 로그인할 수 있다`() {
        mockMvc.perform(
            post("/api/users/sign-in")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(SignInRequest("test", "1234")))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(Json.encodeToString(SignInResponse("test"))))
    }
}
