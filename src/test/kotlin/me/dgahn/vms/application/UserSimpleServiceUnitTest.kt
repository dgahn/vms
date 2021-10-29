package me.dgahn.vms.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import me.dgahn.support.test.UnitTest
import me.dgahn.vms.domain.entity.User
import me.dgahn.vms.exception.custom.PasswordInvalidException
import me.dgahn.vms.exception.custom.UserNotFoundException
import me.dgahn.vms.fixture.getUser
import me.dgahn.vms.infra.UserJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

@UnitTest
class UserSimpleServiceUnitTest {
    @MockK
    lateinit var userJpaRepository: UserJpaRepository

    lateinit var userSimpleService: UserSimpleService

    @BeforeEach
    fun setUp() {
        userSimpleService = UserSimpleService(userJpaRepository)
    }

    @Test
    fun `사용자의 비밀번호를 확인할 수 있다`() {
        val user = getUser()
        every { userJpaRepository.findById(user.id) } returns Optional.of(user)
        val userId = userSimpleService.signIn(user.id, user.password)
        userId shouldBe user.id
    }

    @Test
    fun `사용자가 존재하지 않으면 UserNotFoundException이 발생한다`() {
        val user = getUser()
        every { userJpaRepository.findById(user.id) } returns Optional.empty()
        shouldThrow<UserNotFoundException> { userSimpleService.signIn(user.id, user.password) }
    }

    @Test
    fun `로그인시 비밀번호가 틀리면 PasswordInvalidException이 발생한다`() {
        val user = getUser()
        every { userJpaRepository.findById(user.id) } returns Optional.of(User("test", "1111"))
        shouldThrow<PasswordInvalidException> { userSimpleService.signIn(user.id, user.password) }
    }
}
