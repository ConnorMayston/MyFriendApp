package com.monkeyandriver.myfriendapp.controller

import com.monkeyandriver.myfriendapp.dto.UserDTO
import com.monkeyandriver.myfriendapp.exception.*
import com.monkeyandriver.myfriendapp.requests.LoginRequest
import com.monkeyandriver.myfriendapp.requests.RegisterRequest
import com.monkeyandriver.myfriendapp.service.JwtService
import com.monkeyandriver.myfriendapp.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["v1/users"])
@CrossOrigin
class UserController @Autowired constructor(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    //get
    @GetMapping
    fun getAllUsers(
        @RequestParam
        pageNumber: Int,
        @RequestParam
        query: String
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(userService.getAllUsers(pageNumber, query).map { UserDTO(it) })
    }

    @GetMapping(path = ["{userId}/friends"])
    fun getUserFriends(
        @PathVariable userId: String
    ): List<UserDTO> {
        return userService.getUserFriends(userId).map { UserDTO(it) }
    }

    //put
    @PostMapping(path = ["register"])
    fun register(
        @RequestBody
        body: RegisterRequest
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(
                UserDTO(
                    userService.insertUser(
                        body.username,
                        body.password,
                        body.firstName,
                        body.lastName
                    )
                )
            )
        } catch (exception: UsernameAlreadyExistsException) {
            ResponseEntity.badRequest().body("Username is already taken")
        }
    }

    @PostMapping(path = ["login"])
    fun login(
        @RequestBody
        body: LoginRequest
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(userService.verifyLogin(body.username, body.password))
        } catch (userException: UserDoesNotExistException) {
            ResponseEntity.status(401).build()
        } catch (passwordException: PasswordIncorrectException) {
            ResponseEntity.status(401).build()
        }
    }

    @PostMapping(path = ["{userId}/friends/{friendId}"])
    fun addFriend(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable userId: String,
        @PathVariable friendId: String
    ): ResponseEntity<Any> {
        return try {
            return if (jwtService.parseToken(authHeader.substringAfter("Bearer ")) == userId) {
                userService.insertFriend(userId, friendId)
                ResponseEntity.ok().build()
            } else {
                ResponseEntity.badRequest().body("Jwt Token userId and given userId do not match")
            }
        } catch (userException: UserDoesNotExistException) {
            ResponseEntity.badRequest().body("User does not exist")
        } catch (friendException: FriendDoesNotExistException) {
            ResponseEntity.badRequest().body("Friend does not exist")
        } catch (jwtException: InvalidJwtException) {
            ResponseEntity.badRequest().body("Invalid JWT Token")
        } catch (exception: AddFriendException) {
            ResponseEntity.badRequest().body("Cannot add someone who is already your friend!")
        }
    }

    //delete
    @DeleteMapping(path = ["{userId}/friends/{friendId}"])
    fun removeFriend(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable friendId: String,
        @PathVariable userId: String
    ): ResponseEntity<Any> {
        return try {
            return if (jwtService.parseToken(authHeader.substringAfter("Bearer ")) == userId) {
                userService.removeFriend(userId, friendId)
                ResponseEntity.ok().build()
            } else {
                ResponseEntity.badRequest().body("Jwt Token userId and given userId do not match")
            }
        } catch (exception: UserDoesNotExistException) {
            ResponseEntity.badRequest().body("User does not exist")
        } catch (exception: FriendDoesNotExistException) {
            ResponseEntity.badRequest().body("Friend does not exist")
        } catch (exception: NotFriendException) {
            ResponseEntity.badRequest().body("Friend is not in friend list")
        } catch (exception: InvalidJwtException) {
            ResponseEntity.badRequest().body("Invalid JWT Token")
        }
    }

    @DeleteMapping(path = ["{userId}"])
    fun deleteUser(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable userId: String
    ): ResponseEntity<Any> {
        return try {
            return if (jwtService.parseToken(authHeader.substringAfter("Bearer ")) == userId) {
                userService.deleteUser(userId)
                ResponseEntity.ok().build()
            } else {
                ResponseEntity.badRequest().body("Jwt Token userId and given userId do not match")
            }
        } catch (exception: UserDoesNotExistException) {
            ResponseEntity.badRequest().body("User does not exist")
        } catch (exception: InvalidJwtException) {
            ResponseEntity.badRequest().body("Invalid Jwt Token")
        }
    }
}