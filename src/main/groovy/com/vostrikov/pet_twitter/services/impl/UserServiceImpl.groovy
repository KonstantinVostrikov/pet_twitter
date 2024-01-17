package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.FavoritePost
import com.vostrikov.pet_twitter.dto.Subscription
import com.vostrikov.pet_twitter.dto.UserDto
import com.vostrikov.pet_twitter.entity.User
import com.vostrikov.pet_twitter.exceptions.subscription.SubscriptionException
import com.vostrikov.pet_twitter.exceptions.user.*
import com.vostrikov.pet_twitter.repository.UserRepository
import com.vostrikov.pet_twitter.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
@Transactional
class UserServiceImpl implements UserService {

    @Autowired
    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
    }

    private UserRepository userRepository
    PasswordEncoder passwordEncoder

    @Override
    UserDto createUser(User user) {

        if (user.id != null) throw new UserAlreadyExistException()

        //Check if email is already occupied
        userRepository.findByEmail(user.email).ifPresent { throw new UserWithEmailAlreadyExistException("User with such email already exist") }

        //Check if nickname is already occupied
        userRepository.findByNickName(user.nickName).ifPresent { throw new UserWithNicknameAlreadyExistException() }

        user.setId(UUID.randomUUID().toString())
        user.subscriptions = new HashSet<>()
        user.favoritePosts = new HashSet<>()
        user.password = passwordEncoder.encode(user.password)
        user.role = "USER"
        User createdUser = userRepository.save(user)
        return new UserDto(createdUser)
    }

    @Override
    UserDto findById(String id) {
        Optional<User> optional = userRepository.findById(id)
        if (optional.empty) {
            throw new RuntimeException("User doesn't exist")
        }
        return new UserDto(optional.get())
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
    UserDto edit(User user) {
        // check user is existing
        User foundUser = null
        userRepository.findById(user.id).ifPresentOrElse(it -> foundUser = it, () -> {
            throw new UserNotExistException()
        })

        //check email is the same
        if (!foundUser.email.equals(user.email)) {
            throw new UserEmailCanNotBeChangedException()
        }

        user.password = StringUtils.hasText(user.password) ? passwordEncoder.encode(user.password) : foundUser.password
        def saved = null
        // nickname not changed
        if (foundUser.nickName.equals(user.nickName)) {
            saved = userRepository.save(user)
        }

        // check nickname is free
        if (userRepository.findByNickName(user.nickName).isEmpty()) {
            saved = userRepository.save(user)
        } else {
            throw new UserWithNicknameAlreadyExistException()
        }
        new UserDto(saved)
    }

    @Override
    Boolean subscribe(Subscription subscription) {
        def from = subscription.from
        def to = subscription.to

        // check dto correctness
        if (from == null || from.empty) throw new SubscriptionException()
        if (to == null || to.empty) throw new SubscriptionException()

        // check users exist
        userRepository.findById(to).orElseThrow { new SubscriptionException() }
        def userFrom = userRepository.findById(from).orElseThrow { new SubscriptionException() }

        userFrom.subscriptions.add(to)
        userRepository.save(userFrom)
        return true
    }

    @Override
    Boolean unsubscribe(Subscription subscription) {
        def from = subscription.from
        def to = subscription.to

        // check dto correctness
        if (from == null || from.empty) throw new SubscriptionException()
        if (to == null || to.empty) throw new SubscriptionException()

        // check users exist
        userRepository.findById(to).orElseThrow { new SubscriptionException() }
        def userFrom = userRepository.findById(from).orElseThrow { new SubscriptionException() }

        userFrom.subscriptions.remove(to)
        userRepository.save(userFrom)
        return true
    }

    @Override
    void favoritePost(FavoritePost favoritePost) {
        def user = userRepository.findById(favoritePost.userId).orElseThrow { throw new RuntimeException("User doesn't exist") }
        def contain = user.favoritePosts.contains(favoritePost.postId)

        if (contain) {
            user.favoritePosts.remove(favoritePost.postId)
            userRepository.save(user)
        } else {
            user.favoritePosts.add(favoritePost.postId)
            userRepository.save(user)
        }
    }

    @Override
    User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow { throw new UserNotExistException() }
    }
}
