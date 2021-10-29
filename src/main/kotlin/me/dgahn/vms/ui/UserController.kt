package me.dgahn.vms.ui

import me.dgahn.vms.application.UserSimpleService
import me.dgahn.vms.dto.SignInRequest
import me.dgahn.vms.dto.SignInResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userSimpleService: UserSimpleService
) {

    @PostMapping("/sign-in")
    fun signIn(@RequestBody request: SignInRequest): ResponseEntity<SignInResponse> {
        val response = SignInResponse(userSimpleService.signIn(request.id, request.password))
        return ResponseEntity.ok().body(response)
    }
}
