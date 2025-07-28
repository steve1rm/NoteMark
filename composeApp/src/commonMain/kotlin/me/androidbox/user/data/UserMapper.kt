package me.androidbox.user.data

import me.androidbox.authentication.register.data.UserEntity
import me.androidbox.user.domain.User

fun UserEntity.toUser(): User {
    return User(
        userName = this.userName,
        syncInterval = this.syncInterval,
        syncTimeStamp = this.syncTimeStamp
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userName = this.userName,
        syncInterval = this.syncInterval,
        syncTimeStamp = this.syncTimeStamp
    )
}