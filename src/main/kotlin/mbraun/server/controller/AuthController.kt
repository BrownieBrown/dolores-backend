package mbraun.server.controller

import mbraun.server.dto.SignInDTO
import mbraun.server.dto.SignUpDTO
import mbraun.server.model.User
import mbraun.server.service.RoleService
import mbraun.server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class AuthController(private val userService: UserService, private val roleService: RoleService) {

    private val encoder = BCryptPasswordEncoder()

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody body: SignUpDTO): User {
        val user = User()
        user.email = body.email
        user.password = encoder.encode(body.password.toString())
        user.roles = mutableListOf(roleService.getRoleByName("USER"))

        return userService.createUser(user)
    }

    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    fun signIn(@RequestBody body: SignInDTO): User {
        val user = userService.getUserByEmail(body.email)

        userService.comparePassword(body.password, user.password)

        return user
    }
}