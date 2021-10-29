package me.dgahn.vms.infra

import me.dgahn.vms.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, String>
