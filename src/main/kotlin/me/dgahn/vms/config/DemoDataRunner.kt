package me.dgahn.vms.config

import me.dgahn.vms.domain.entity.User
import me.dgahn.vms.infra.UserJpaRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class DemoDataRunner(
    private val userJpaRepository: UserJpaRepository,
    private val env: Environment
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        if (env.activeProfiles.first() != "test") {
            userJpaRepository.save(User("test", "1234"))
        }
    }
}
