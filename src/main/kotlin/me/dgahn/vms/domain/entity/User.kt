package me.dgahn.vms.domain.entity

import me.dgahn.vms.exception.custom.UserVacationsUpdateException
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

const val DEFAULT_TOTAL_VACATIONS = 15.0
const val MIN_USED_VACATION = 0.0

@Entity
@Table(name = "users")
class User(
    @Id
    val id: String,
    val password: String,
    val totalVacations: Double = DEFAULT_TOTAL_VACATIONS,
    var usedVacations: Double = 0.0
) {
    val remainVacations: Double
        get() = totalVacations - usedVacations

    fun confirmPassword(password: String) = this.password == password
    fun isPossibleVacations(useVacations: Double) = usedVacations + useVacations in MIN_USED_VACATION..totalVacations
    fun updateUsedVacations(useVacations: Double) {
        if (!isPossibleVacations(useVacations)) {
            throw UserVacationsUpdateException()
        }
        this.usedVacations += useVacations
    }
}
