package com.example.teachersjournal.data.groups

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroupDao {

    @Insert
    suspend fun addGroup(group: GroupData)

    @Query("SELECT * FROM groups ORDER BY group_name ASC")
    suspend fun getAllGroups(): MutableList<GroupData>


    @Query("DELETE FROM groups WHERE group_name = :group")
    suspend fun deleteGroup(group: String)


}