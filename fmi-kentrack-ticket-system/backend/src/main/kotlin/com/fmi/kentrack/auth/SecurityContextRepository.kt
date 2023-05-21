package com.fmi.kentrack.auth

import com.fmi.kentrack.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class SecurityContextRepository : ServerSecurityContextRepository {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val token = exchange.request.cookies.getFirst("token")?.value
        val refreshToken = exchange.request.cookies.getFirst("refreshToken")?.value
        return if (token.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            Mono.empty()
        } else {
            authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(token, token))
                .switchIfEmpty(
                    authenticationManager.authenticate(UsernamePasswordAuthenticationToken(refreshToken, refreshToken))
                        .map { auth ->
                            val userDetails = UserDetailsImpl(userService.getUserByUsername(auth.principal.toString())!!)
                            val newAccessToken = jwtUtil.generateToken(userDetails)
                            val newRefreshToken = jwtUtil.generateRefreshToken(userDetails)
                            exchange.response.addCookie(generateCookie(newAccessToken, "token"))
                            exchange.response.addCookie(generateCookie(newRefreshToken, "refreshToken"))
                            auth
                        }
                )
                .map(::SecurityContextImpl)
        }
    }

}

private fun generateCookie(token: String, cookieName: String): ResponseCookie = ResponseCookie.from(cookieName, token)
    .path("/")
    .httpOnly(true)
    .sameSite("strict")
    .build()
