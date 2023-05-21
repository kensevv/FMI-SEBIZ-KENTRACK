package com.fmi.kentrack.auth

import com.fmi.kentrack.users.User
import com.fmi.kentrack.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange


@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/auth")
class AuthenticationController {

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/login")
    suspend fun login(
        @RequestBody request: AuthRequest,
        exchange: ServerWebExchange
    ): ResponseEntity<AuthenticationResponse> {
        require(request.username.isNotBlank() && request.password.isNotBlank()) { "Blank username or password" }
        exchange.response.addCookie(generateCookie("", "token"))// reset cookie
        exchange.response.addCookie(generateCookie("", "refreshToken"))// reset cookie
        return when (val authResponse = isAuthenticated(request.username, request.password)) {
            is AuthenticationResponse.Success ->
                UserDetailsImpl(authResponse.user)
                    .let {
                        val token = jwtUtil.generateToken(it)
                        val refreshToken = jwtUtil.generateRefreshToken(it)
                        exchange.response.addCookie(generateCookie(token, "token"))
                        exchange.response.addCookie(generateCookie(refreshToken, "refreshToken"))
                        ResponseEntity.ok(authResponse)
                    }

            is AuthenticationResponse.Error -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse)
        }
    }

    @PostMapping("/logout")
    suspend fun logout(exchange: ServerWebExchange) {
        exchange.response.addCookie(generateCookie("", "token"))
        exchange.response.addCookie(generateCookie("", "refreshToken"))
    }

    private fun generateCookie(token: String, cookieName: String) = ResponseCookie.from(cookieName, token)
        .path("/")
        .httpOnly(true)
        .sameSite("strict")
        .build()

    private fun isAuthenticated(username: String, password: String): AuthenticationResponse =
        runCatching {
            userService.getUserByUsername(username)?.let {
                when (userService.usernamePasswordAuthenticate(username, password)) {
                    true -> AuthenticationResponse.Success(it)
                    false -> AuthenticationResponse.Error(AuthenticationResult.WRONG_PASSWORD)
                }
            } ?: AuthenticationResponse.Error(AuthenticationResult.UNKNOWN_USER)
        }.getOrDefault(AuthenticationResponse.Error(AuthenticationResult.UNSUCCESSFUL_ATTEMPT))
}

sealed class AuthenticationResponse {
    data class Success(val user: User) : AuthenticationResponse()
    data class Error(val result: AuthenticationResult) : AuthenticationResponse()
}

enum class AuthenticationResult {
    UNKNOWN_USER,
    WRONG_PASSWORD,
    UNSUCCESSFUL_ATTEMPT
}
