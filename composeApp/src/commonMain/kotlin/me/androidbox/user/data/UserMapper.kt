package me.androidbox.user.data

import me.androidbox.authentication.register.data.UserEntity
import me.androidbox.user.domain.User

fun UserEntity.toUser(): User {
    return User(
        userId = this.userId,
        userName = this.userName,
        email = this.email,
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        userName = this.userName,
        email = this.email,
    )
}