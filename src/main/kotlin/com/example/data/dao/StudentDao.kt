package com.example.data.dao

import com.example.data.Student

interface StudentDao {
    //Suspend task should perform background
    suspend fun insert(
        name:String,
        age:Int
    ):Student?

    suspend fun deleteById(userId: Int):Int?

    suspend fun updateById(
        userId: Int,
        name:String,
        age:Int
    ):Int

    suspend fun getAllStudents():List<Student>

    suspend fun getStudentById(userId:Int):Student?

}