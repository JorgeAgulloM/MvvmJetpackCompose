package com.softyorch.mvvmjetpackcompose.data.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user_table WHERE id=:userId")
    fun getUser(userId: UUID): Flow<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}
