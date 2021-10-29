package me.dgahn.vms.ui

import me.dgahn.vms.application.VacationSimpleService
import me.dgahn.vms.dto.CreateVacationRequest
import me.dgahn.vms.dto.CreateVacationResponse
import me.dgahn.vms.dto.DeleteVacationResponse
import me.dgahn.vms.dto.VacationListResponse
import me.dgahn.vms.dto.VacationResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vacations")
class VacationController(
    private val vacationSimpleService: VacationSimpleService
) {

    @PostMapping
    fun create(@RequestBody request: CreateVacationRequest): ResponseEntity<CreateVacationResponse> {
        return ResponseEntity(vacationSimpleService.create(request), HttpStatus.CREATED)
    }

    @GetMapping
    fun findBy(
        @RequestParam("user-id") userId: String,
        @RequestParam("offset-vacation-id") offsetVacationId: Long,
        @RequestParam("limit") limit: Int
    ): ResponseEntity<VacationListResponse> {
        val vacations = vacationSimpleService.findAllBy(userId, offsetVacationId, limit).map { VacationResponse(it) }
        return ResponseEntity.ok().body(VacationListResponse(vacations))
    }

    @GetMapping("/{vacation-id}")
    fun findBy(@PathVariable("vacation-id") id: Long): ResponseEntity<VacationResponse> =
        ResponseEntity.ok().body(VacationResponse(vacationSimpleService.findById(id)))

    @DeleteMapping("/{vacation-id}")
    fun deleteById(@PathVariable("vacation-id") id: Long): ResponseEntity<DeleteVacationResponse> {
        vacationSimpleService.deleteById(id)
        return ResponseEntity.ok(DeleteVacationResponse(id))
    }
}
