package me.androidbox.user.data

import me.androidbox.authentication.register.data.UserEntity
import me.androidbox.user.domain.User

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        userName = this.userName,
        email = this.email,
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        userName = this.userName,
        email = this.email,
    )
}