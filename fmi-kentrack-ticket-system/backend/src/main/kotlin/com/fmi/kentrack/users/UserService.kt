package com.fmi.kentrack.users


import com.fmi.kentrack.jooq.tables.records.UserRecord
import com.fmi.kentrack.jooq.tables.references.USER
import com.fmi.kentrack.services.BaseService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService : BaseService() {
    @Autowired
    @Qualifier("secondary-encoder")
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    fun usernamePasswordAuthenticate(username: String, password: String) =
        passwordEncoder.matches(password, getUserRecordByUsername(username)?.password!!)

    fun fetchAllUsers(): List<User> = db.fetch(USER, USER.DELETED.eq("N")).map { it.mapToInternalModel() }

    fun getUserByUsername(username: String): User? = getUserRecordByUsername(username)?.mapToInternalModel()
    fun checkIfUsernameExists(username: String): Boolean = getUserRecordByUsername(username) != null

    private fun getUserRecordByUsername(userName: String, dslContext: DSLContext = db): UserRecord? =
        dslContext.fetchOne(USER, USER.USERNAME.eq(userName).and(USER.DELETED.eq("N")))

    @PreAuthorize("hasAuthority('MAINTAINER')")
    suspend fun updateUser(user: User): User? = db.transactionResult { transaction ->
        getUserRecordByUsername(user.username, transaction.dsl())?.apply {
            from(user)
            roles = Json.encodeToString(user.roles)
            update()
        }?.mapToInternalModel()
    }

    @PreAuthorize("hasAuthority('MAINTAINER')")
    suspend fun createUser(user: User): User? = db.transactionResult { transaction ->
        kotlin.runCatching {
            (transaction.dsl().fetchOne(USER, USER.USERNAME.eq(user.username))?.apply {
                from(user)
                roles = Json.encodeToString(listOf(Roles.DEVELOPER))
                password = passwordEncoder.encode("easyTrackTest")
                deleted = "N"
            } ?: transaction.dsl().newRecord(USER).apply {
                from(user)
                roles = Json.encodeToString(listOf(Roles.DEVELOPER))
                deleted = "N"
                password = passwordEncoder.encode("easyTrackTest")
                insert()
            }).mapToInternalModel()
        }.getOrNull()
    }

    @PreAuthorize("hasAuthority('MAINTAINER')")
    suspend fun deleteUser(id: String): String? =
        db.transactionResult { transaction ->
            getUserRecordByUsername(id, transaction.dsl())?.apply {
                deleted = "Y"
                update()
            }
            id
        }
    fun changePassword(currentPassword: String, newPassword: String, currentUserUsername: String): Boolean {
        val dbUser = getUserRecordByUsername(currentUserUsername)
        return passwordEncoder.matches(currentPassword, dbUser?.password) &&
                dbUser?.apply {
                    password = passwordEncoder.encode(newPassword)
                }?.update()?.let {
                    it > 0
                } ?: false
    }
    fun getUsersByUsernameList(usernames: List<String>) = usernames.map { getUserByUsername(it)!! }
}
fun UserRecord.mapToInternalModel(): User = User(
    username = username!!,
    email = email,
    firstName = firstName,
    lastName = lastName,
    roles = roles?.let { Json.decodeFromString<List<String>>(it).map { role -> Roles.valueOf(role) } }
        ?: listOf(Roles.DEVELOPER)
)
