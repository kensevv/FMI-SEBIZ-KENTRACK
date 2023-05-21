package com.fmi.kentrack.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@Component
class JWTUtil(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.token.expiration}") private val expirationTime: String,
    @Value("\${jwt.refreshtoken.expiration}") private val refreshTokenExpiration: String,
) {
    private var key = lazy { Keys.hmacShaKeyFor(secret.toByteArray()) }

    fun getAllClaimsFromToken(token: String?): Claims =
        Jwts.parserBuilder().setSigningKey(key.value).build().parseClaimsJws(token).body

    fun getUserUuidFromToken(token: String?): String = getAllClaimsFromToken(token).subject

    fun getExpirationDateFromToken(token: String?): Date = getAllClaimsFromToken(token).expiration

    private fun isTokenExpired(token: String): Boolean = getExpirationDateFromToken(token).before(Date())

    fun generateToken(userDetails: UserDetailsImpl): String = doGenerateToken(
        mutableMapOf<String, Any>(
            "role" to userDetails.user.roles,
        ), userDetails.user.username, expirationTime.toLong()
    )

    fun generateRefreshToken(userDetails: UserDetailsImpl): String = doGenerateToken(
        mutableMapOf<String, Any>(
            "role" to userDetails.user.roles,
        ), userDetails.user.username, refreshTokenExpiration.toLong()
    )

    private fun doGenerateToken(claims: Map<String, Any>, userUUID: String, expirationTime: Long): String {
        val createdDate = LocalDateTime.now()
        val expirationDate = createdDate.plusSeconds(expirationTime)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userUUID)
            .setIssuedAt(Date.from(createdDate.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(key.value)
            .compact()
    }

    fun validateToken(token: String): Boolean = kotlin.runCatching {
        !isTokenExpired(token)
    }.getOrElse {
        when (it) {
            is ExpiredJwtException -> false
            else -> throw it
        }
    }
}
