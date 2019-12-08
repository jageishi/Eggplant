package org.ageage.eggplant.common.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "oauth_token") val oAuthToken: String,
    @ColumnInfo(name = "oauth_token_secret") val oAuthTokenSecret: String
)