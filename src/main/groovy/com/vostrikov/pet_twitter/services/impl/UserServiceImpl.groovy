package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.User
import com.vostrikov.pet_twitter.exceptions.user.UserAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.user.UserEmailCanNotBeChangedException
import com.vostrikov.pet_twitter.exceptions.user.UserNotExistException
import com.vostrikov.pet_twitter.exceptions.user.UserWithEmailAlreadyExistException
import com.vostrikov.pet_twitter.exceptions.user.UserWithNicknameAlreadyExistException
import com.vostrikov.pet_twitter.repository.UserRepository
import com.vostrikov.pet_twitter.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    private UserRepository userRepository

    @Override
    User createUser(User user) {

        if (user.id != null) throw new UserAlreadyExistException()

        //Check if email is already occupied
        userRepository.findByEmail(user.email).ifPresent { throw new UserWithEmailAlreadyExistException("User with such email already exist") }

        //Check if nickname is already occupied
        userRepository.findByNickName(user.nickName).ifPresent { throw new UserWithNicknameAlreadyExistException() }

        user.setId(UUID.randomUUID().toString())
        User createdUser = userRepository.save(user)
        return createdUser
    }

    @Override
    User findById(String id) {
        Optional<User> optional = userRepository.findById(id)
        if (optional.empty) {
            throw new RuntimeException("User doesn't exist")
        }
        return optional.get()
    }

    @Override
    void deleteById(String id) {
        Optional<User> optional = userRepository.findById(id)
        if (optional.empty) {
            throw new RuntimeException("User doesn't exist")
        }
        userRepository.deleteById(id)
    }

    @Override
    User edit(User user) {
        // check user is existing
        User foundUser = null
        userRepository.findById(user.id).ifPresentOrElse(it -> foundUser = it, () -> {
            throw new UserNotExistException()
        })

        //check email is the same
        if (!foundUser.email.equals(user.email)) {
            throw new UserEmailCanNotBeChangedException()
        }

        // nickname not changed
        if (foundUser.nickName.equals(user.nickName)) {
            return userRepository.save(user)
        }

        // check nickname is free
        if (userRepository.findByNickName(user.nickName).isEmpty()) {
            return userRepository.save(user)
        } else {
            throw new UserWithNicknameAlreadyExistException()
        }

    }
}
