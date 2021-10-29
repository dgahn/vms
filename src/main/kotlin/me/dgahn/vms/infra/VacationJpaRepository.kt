package me.dgahn.vms.infra

import me.dgahn.vms.domain.entity.Vacation
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VacationJpaRepository : JpaRepository<Vacation, Long> {
    @Query(
        value = "SELECT v FROM Vacation v JOIN v.user u WHERE u.id = :userId AND v.id > :offsetVacationId ORDER BY v.id"
    )
    fun findAllBy(userId: String, offsetVacationId: Long, pageable: Pageable): List<Vacation>
}
