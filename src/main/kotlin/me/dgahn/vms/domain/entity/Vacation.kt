package me.dgahn.vms.domain.entity

import me.dgahn.support.domain.entity.BaseEntity
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "vacations")
class Vacation(
    @Enumerated(EnumType.STRING)
    val type: VacationType,
    val comment: String,
    val date: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    id: Long = 0L
) : BaseEntity(id) {
    val isUsed: Boolean
        get() = !LocalDate.now().isBefore(date)
}
