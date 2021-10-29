package me.dgahn.vms.domain.entity

import io.kotest.assertions.throwables.shouldThrow
import me.dgahn.vms.exception.custom.UserVacationsUpdateException
import me.dgahn.vms.fixture.getUser
import org.junit.jupiter.api.Test

internal class UserTest {

    @Test
    fun `휴가가 남아있지 않으면 휴가를 추가할 수 없다`() {
        val user = getUser(usedVacations = 15.0)
        shouldThrow<UserVacationsUpdateException> { user.updateUsedVacations(1.0) }
    }

    @Test
    fun `사용된 휴가가 없는데 휴가를 줄일 수는 없다`() {
        val user = getUser()
        shouldThrow<UserVacationsUpdateException> { user.updateUsedVacations(-1.0) }
    }
}
