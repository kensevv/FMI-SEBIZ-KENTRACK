package com.fmi.kentrack.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager : ReactiveAuthenticationManager {
    @Autowired
    private lateinit var jwtUtil: JWTUtil

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()
        return Mono.just(
            jwtUtil.validateToken(authToken)
        )
            .filter {
                it
            }
            .switchIfEmpty(Mono.empty())
            .map {
                val userUUID = jwtUtil.getUserUuidFromToken(authToken)
                val claims = jwtUtil.getAllClaimsFromToken(authToken)
                val rolesMap = claims.get("role", List::class.java)
                UsernamePasswordAuthenticationToken(
                    userUUID,
                    null,
                    rolesMap.map { SimpleGrantedAuthority(it?.toString()) }
                )
            }
    }
}