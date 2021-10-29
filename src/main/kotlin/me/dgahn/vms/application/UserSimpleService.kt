package me.dgahn.vms.application

import me.dgahn.vms.exception.custom.PasswordInvalidException
import me.dgahn.vms.exception.custom.UserNotFoundException
import me.dgahn.vms.infra.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserSimpleService(
    private val userJpaRepository: UserJpaRepository
) {
    fun signIn(id: String, password: String): String {
        val user = userJpaRepository.findById(id).orElseThrow { throw UserNotFoundException() }
        if (!user.confirmPassword(password)) {
            throw PasswordInvalidException()
        }
        return user.id
    }
}
