package org.ageage.eggplant.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import org.ageage.eggplant.common.db.entity.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)
}