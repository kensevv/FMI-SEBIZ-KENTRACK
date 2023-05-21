package com.fmi.kentrack.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/all")
    fun getUsers() = userService.fetchAllUsers()

    @GetMapping("/")
    fun getUser(@RequestParam username: String): User? {
        return userService.getUserByUsername(username)
    }

    @GetMapping("/exists")
    fun checkIfUsernameExists(@RequestParam username: String): Boolean {
        return userService.checkIfUsernameExists(username)
    }

    @GetMapping("/change-password")
    fun changePassword(
        @RequestParam currentPassword: String,
        @RequestParam newPassword: String,
        principal: Principal
    ) = userService.changePassword(currentPassword, newPassword, principal.name)

    @PreAuthorize("hasAuthority('MAINTAINER')")
    @PostMapping("/create")
    suspend fun createUser(@RequestBody user: User) = userService.createUser(user)

    @PreAuthorize("hasAuthority('MAINTAINER')")
    @PostMapping("/update")
    suspend fun updateUser(@RequestBody user: User) = userService.updateUser(user)

    @PreAuthorize("hasAuthority('MAINTAINER')")
    @DeleteMapping("/delete")
    suspend fun deleteUser(@RequestParam username: String) = userService.deleteUser(username)

}
