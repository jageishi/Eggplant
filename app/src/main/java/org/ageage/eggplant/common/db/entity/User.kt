package org.ageage.eggplant.common.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "oauth_token") val oAuthToken: String,
    @ColumnInfo(name = "oauth_token_secret") val oAuthTokenSecret: String
)