package me.androidbox.user.data.imp

import me.androidbox.core.models.DataError
import me.androidbox.user.data.UserLocalDataSource
import me.androidbox.user.data.toUser
import me.androidbox.user.data.toUserEntity
import me.androidbox.user.domain.User
import me.androidbox.user.domain.UserRepository
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

class UserRepositoryImp(
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun saveUser(user: User) {
        userLocalDataSource.saveUser(user.toUserEntity())
    }

    override suspend fun fetchUser(userName: String): Either<User, DataError.Local> {
        val result = userLocalDataSource.fetchUserByUserName(userName)

        return when(result) {
            is Left -> {
                Left(result.left.toUser())
            }
            is Right -> {
                Right(result.right)
            }
        }
    }
}