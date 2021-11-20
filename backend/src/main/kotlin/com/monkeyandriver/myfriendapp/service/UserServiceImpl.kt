package com.monkeyandriver.myfriendapp.service

import com.monkeyandriver.myfriendapp.domain.User
import com.monkeyandriver.myfriendapp.dto.JwtTokenDTO
import com.monkeyandriver.myfriendapp.exception.*
import com.monkeyandriver.myfriendapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val encoder: PasswordEncoder
) : UserService {
    override fun insertUser(username: String, password: String, firstName: String, lastName: String): User {
        if (userRepository.existsByUsername(username)) {
            throw UsernameAlreadyExistsException()
        } else {

            val user = User(username, encoder.encode(password), firstName, lastName)
            return userRepository.save(user)
        }
    }

    @Transactional
    override fun deleteUser(userId: String) {
        if (userRepository.existsById(userId)) {
            val user = userRepository.findById(userId).orElseThrow { UserDoesNotExistException() }
            val all = userRepository.findAll()
            all.forEach {
                if (it.getFriendsList().contains(user)) {
                    it.removeFriend(user)
                    userRepository.save(it)
                }
            }
            userRepository.deleteById(userId)
        } else {
            throw UserDoesNotExistException()
        }
    }

    override fun insertFriend(userId: String, friendId: String) {
        val user = userRepository.findById(userId).orElseThrow { UserDoesNotExistException() }
        val friend = userRepository.findById(friendId).orElseThrow { FriendDoesNotExistException() }


        if (user != friend && !user.getFriendsList().contains(friend)) {
            user.addFriend(friend)
            userRepository.save(user)
        } else {
            throw AddFriendException()
        }
    }

    override fun removeFriend(userId: String, friendId: String) {
        val user = userRepository.findById(userId).orElseThrow { UserDoesNotExistException() }
        val friend = userRepository.findById(friendId).orElseThrow { FriendDoesNotExistException() }

        if (user.getFriendsList().contains(friend)) {
            user.removeFriend(friend)
            userRepository.save(user)
        } else {
            throw NotFriendException()
        }
    }

    override fun getUser(userId: String): User {
        return userRepository.findById(userId).orElseThrow { UserDoesNotExistException() }
    }

    override fun getAllUsers(pageNumber: Int, query: String): List<User> {
        return if (query == "blank") {
            userRepository.findAll(PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "username"))).content
        } else {
            userRepository.findAllByUsernameContaining(
                query,
                PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "username"))
            )
        }
    }

    override fun getUserFriends(userId: String): List<User> {
        val user = userRepository.findById(userId).orElseThrow { UserDoesNotExistException() }

        return user.getFriendsList()
    }

    override fun verifyLogin(username: String, password: String): JwtTokenDTO {

        if (userRepository.existsByUsername(username)) {
            val user = userRepository.findByUsername(username)
            if (encoder.matches(password, user.password)) {
                return JwtTokenDTO(jwtService.generateToken(user.id))
            } else {
                throw PasswordIncorrectException()
            }
        } else {
            throw UserDoesNotExistException()
        }
    }
}