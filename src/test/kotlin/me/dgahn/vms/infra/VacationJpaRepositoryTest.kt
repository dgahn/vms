package me.dgahn.vms.infra

import io.kotest.matchers.collections.shouldHaveSize
import me.dgahn.support.test.RepositoryTest
import me.dgahn.vms.domain.entity.User
import me.dgahn.vms.fixture.getUser
import me.dgahn.vms.fixture.getVacations
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest

@RepositoryTest
class VacationJpaRepositoryTest(
    private val userJpaRepository: UserJpaRepository,
    private val vacationJpaRepository: VacationJpaRepository
) {
    lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = userJpaRepository.save(getUser())
        vacationJpaRepository.saveAll(getVacations(user, 1, 20))
    }

    @AfterEach
    fun cleanUp() {
        vacationJpaRepository.deleteAll()
    }

    @Test
    fun `사용자 정보로 휴가 목록을 조회할 수 있다`() {
        val limit = 5
        val pageable = PageRequest.ofSize(limit)
        val vacations = vacationJpaRepository.findAllBy(user.id, 5, pageable)
        vacations shouldHaveSize limit
    }
}
