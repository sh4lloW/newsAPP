package com.example.newsapp.room

import androidx.room.*

interface NewStarDao {
    //增
    @Insert
    fun insertUser(user: NewStar): Long
    //查找全部
    @Query("select * from NewStar where user = :user")
    fun loadAllUsers(user:String): List<NewStar>
    //查   ID相同  user相同
    @Query("select * from NewStar where keyID = :ID and user=:user")
    fun loadUsersOlderThan(ID: String,user:String): List<NewStar>
    //删  ID相同  user相同
    @Query("delete from NewStar where keyID = :ID and user=:user")
    fun deleteUserByLastName(ID: String,user:String): Int

}