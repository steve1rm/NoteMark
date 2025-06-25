package me.androidbox.user.data.imp

import me.androidbox.authentication.register.UserEntity
import me.androidbox.core.data.NoteMarkDatabase
import me.androidbox.core.models.DataError
import me.androidbox.user.data.UserLocalDataSource
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

class UserLocalDataSourceImp(
    private val noteMarkDatabase: NoteMarkDatabase
) : UserLocalDataSource {
    override suspend fun saveUser(userEntity: UserEntity) : Either<Long, DataError.Local> {
        val result = noteMarkDatabase.noteMarkDao().insertUser(userEntity)

        return if(result >= 0) {
            Left(result)
        }
        else {
            Right(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun fetchUser(): Either<UserEntity?, DataError.Local> {
        val userEntity = noteMarkDatabase.noteMarkDao().getUser()

        return if(userEntity == null) {
            Left(userEntity)
        }
        else {
            return Right(DataError.Local.EMPTY)
        }
    }
}