package com.recap.api.global.security

import com.recap.core.domain.user.entity.Role
import com.recap.core.domain.user.entity.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class RecapAuthentication(
    val id: Long,
    val roles: Set<Role>
) : Authentication {
    companion object {
        private const val AUTHORITY_PREFIX = "ROLE_"

        fun from(payload: Map<String, *>): RecapAuthentication =
            with(payload) {
                RecapAuthentication(
                    id = (get(User::id.name) as String).toLong(),
                    roles =
                        (get(User::roles.name) as String)
                            .split(',')
                            .map { Role.valueOf(it) }
                            .toSet()
                )
            }
    }

    override fun getAuthorities(): Set<GrantedAuthority> =
        roles
            .map { SimpleGrantedAuthority(AUTHORITY_PREFIX + it.name) }
            .toSet()

    override fun getName(): String? = null

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any? = null

    override fun getPrincipal(): Long = id

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean): Unit = throw UnsupportedOperationException()
}
