package com.vostrikov.pet_twitter.controllers

import com.vostrikov.pet_twitter.dto.ResponseResult
import com.vostrikov.pet_twitter.dto.User
import com.vostrikov.pet_twitter.exceptions.user.UserWithEmailAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.user.UserWithNicknameAlreadyExistException
import com.vostrikov.pet_twitter.services.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController()
@RequestMapping("/users")
class UserController {
    @Autowired
    UserService userService

    @PostMapping("/create")
    def createUser(@RequestBody User user) {
        try {
            def newUser = userService.createUser(user)
            log.debug("User created successfully $newUser.id")
            return ResponseEntity.ok().body(new ResponseResult("User successfully created", newUser))
        } catch (UserWithEmailAlreadyExistException exception) {
            log.debug("Failing to create user - user with email:{$user.email} already exist ")
            return ResponseEntity.internalServerError().body(new ResponseResult(exception.message))
        } catch (UserWithNicknameAlreadyExistException exception) {
            log.debug("Failing to create user - user with nickname:{$user.nickName} already exist ")
            return ResponseEntity.internalServerError().body(new ResponseResult(exception.message))
        } catch (Exception exception) {
            log.error(exception.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(exception.message))
        }
    }

    @PostMapping("/edit")
    def editUser(@RequestBody User user) {
        try {
            def editedUser = userService.edit(user)
            return ResponseEntity.ok().body(new ResponseResult("User successfully changed", editedUser))
        } catch (Exception exception) {
            log.error(exception.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(exception.message))
        }
    }

    @DeleteMapping("/delete/{id}")
    def deleteUser(@PathVariable("id") String id) {
        try {
            userService.deleteById(id)
            return ResponseEntity.ok().body(new ResponseResult("User with id $id deleted successfully"))
        } catch (Exception exception) {
            log.error(exception.message)
            return ResponseEntity.internalServerError().body(new ResponseResult(exception.message))
        }
    }

}
