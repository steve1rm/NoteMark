package me.androidbox.user.data

import me.androidbox.authentication.register.UserEntity
import me.androidbox.user.domain.User

fun UserEntity.toUser(): User {
    return User(
        userName = this.userName,
        email = this.email,
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userName = this.userName,
        email = this.email,
    )
}