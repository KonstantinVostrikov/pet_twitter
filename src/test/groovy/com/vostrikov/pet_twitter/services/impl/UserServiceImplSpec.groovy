package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.User
import com.vostrikov.pet_twitter.exceptions.user.UserAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.user.UserEmailCanNotBeChangedException
import com.vostrikov.pet_twitter.exceptions.user.UserNotExistException
import com.vostrikov.pet_twitter.exceptions.user.UserWithEmailAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.user.UserWithNicknameAlreadyExistException
import com.vostrikov.pet_twitter.repository.UserRepository
import com.vostrikov.pet_twitter.services.UserService
import spock.lang.Specification

class UserServiceImplSpec extends Specification {


    def userRepository = Mock(UserRepository.class)
    UserService userService = new UserServiceImpl(userRepository)

    //create
    def "createUser should save a new user"() {
        given:
        def user = new User(email: "test@example.com", nickName: "testUser")
        userRepository.save(user) >> user
        userRepository.findByEmail("test@example.com") >> Optional.empty()
        userRepository.findByNickName("testUser") >> Optional.empty()

        when:
        def createdUser = userService.createUser(user)


        then:
        createdUser.id != null
        createdUser.email == "test@example.com"
        createdUser.nickName == "testUser"
        1 * userRepository.findByEmail("test@example.com") >> Optional.empty()
        1 * userRepository.findByNickName("testUser") >> Optional.empty()
        1 * userRepository.save(user) >> user

    }

    def "createUser should throw UserAlreadyExistException if user from front has id"() {
        given:
        def user = new User(id: "123", email: "existing@example.com", nickName: "testUser")

        when:
        userService.createUser(user)

        then:
        thrown(UserAlreadyExistException)
    }

    def "createUser should throw UserWithEmailAlreadyExistException if email is already occupied"() {
        given:
        def user = new User(email: "existing@example.com", nickName: "testUser")
        userRepository.findByEmail("existing@example.com") >> Optional.of(new User())

        when:
        userService.createUser(user)

        then:
        thrown(UserWithEmailAlreadyExistException)
    }

    def "createUser should throw UserWithNicknameAlreadyExistException if nickname is already occupied"() {
        given:
        def user = new User(email: "test@example.com", nickName: "existingUser")
        userRepository.findByEmail("test@example.com") >> Optional.empty()
        userRepository.findByNickName("existingUser") >> Optional.of(new User())

        when:
        userService.createUser(user)

        then:
        thrown(UserWithNicknameAlreadyExistException)
    }


    // Find
    def "findById should return the user if it exists"() {
        given:
        def userId = "123"
        def existingUser = new User(id: userId, email: "test@example.com", nickName: "testUser")
        userRepository.findById(userId) >> Optional.of(existingUser)

        when:
        def foundUser = userService.findById(userId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(existingUser)

        foundUser == existingUser
    }

    def "findById should throw RuntimeException if the user doesn't exist"() {
        given:
        def userId = "456"
        userRepository.findById(userId) >> Optional.empty()

        when:
        userService.findById(userId)

        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        def e = thrown(RuntimeException)
        e.message == "User doesn't exist"
    }


    // delete
    def "deleteById should delete the user if it exists"() {
        given:
        String userId = "123"
        userRepository.findById(userId) >> Optional.of(new User())
        userRepository.deleteById(userId) >> {}

        when:
        userService.deleteById(userId)

        then:
        1 * userRepository.deleteById(userId)
    }

    def "deleteById should not throw an exception if the user doesn't exist"() {
        given:
        def userId = "456"
        userRepository.findById(userId) >> Optional.empty()
        userRepository.deleteById(userId) >> { throw new RuntimeException() }

        when:
        userService.deleteById(userId)

        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        def e = thrown(RuntimeException)
        e.message == "User doesn't exist"
    }


    // edit
    def "edit should update the user if it exists and nickname is changed to a free one"() {
        given:
        def existingUser = new User(id: "123", email: "test@example.com", nickName: "oldNickName")
        def editedUser = new User(id: "123", email: "test@example.com", nickName: "newNickName")
        userRepository.findById("123") >> Optional.of(existingUser)
        userRepository.findByNickName("newNickName") >> Optional.empty()
        userRepository.save(_ as User) >> editedUser

        when:
        userService.edit(new User(id: "123", email: "test@example.com", nickName: "newNickName"))

        then:
        1 * userRepository.findById("123") >> Optional.of(existingUser)
        1 * userRepository.findByNickName("newNickName") >> Optional.empty()
        1 * userRepository.save(_ as User) >> editedUser

        editedUser.nickName == "newNickName"
    }


    def "edit should throw UserNotExistException if the user doesn't exist"() {
        given:
        userRepository.findById("456") >> Optional.empty()

        when:
        userService.edit(new User(id: "456", email: "test@example.com", nickName: "newNickName"))

        then:
        thrown(UserNotExistException)
    }

    def "edit should throw UserEmailCanNotBeChangedException if email is changed"() {
        given:
        def existingUser = new User(id: "789", email: "old@example.com", nickName: "oldNickName")
        userRepository.findById("789") >> Optional.of(existingUser)

        when:
        userService.edit(new User(id: "789", email: "new@example.com", nickName: "oldNickName"))

        then:
        thrown(UserEmailCanNotBeChangedException)
    }

    def "edit should throw UserWithNicknameAlreadyExistException if nickname is already occupied"() {
        given:
        def existingUser = new User(id: "101", email: "test@example.com", nickName: "oldNickName")
        userRepository.findById("101") >> Optional.of(existingUser)
        userRepository.findByNickName("existingNickName") >> Optional.of(new User())

        when:
        userService.edit(new User(id: "101", email: "test@example.com", nickName: "existingNickName"))

        then:
        thrown(UserWithNicknameAlreadyExistException)
    }
}
