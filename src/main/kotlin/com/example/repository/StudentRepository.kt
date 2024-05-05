package com.example.repository

import com.example.data.Student
import com.example.data.StudentTable
import com.example.data.dao.StudentDao
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import javax.xml.crypto.Data

class StudentRepository:StudentDao {

    override suspend fun insert(name: String, age: Int): Student? {
        var statment:InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statment = StudentTable.insert {student->
                student[StudentTable.name] = name
                student[StudentTable.age] = age
            }
        }
        return rowToData(statment?.resultedValues?.get(0))
    }

    override suspend fun deleteById(userId: Int):Int =
        DatabaseFactory.dbQuery {
            StudentTable.deleteWhere { StudentTable.userId.eq(userId)}
        }

    override suspend fun updateById(userId: Int, name: String, age: Int): Int=
        DatabaseFactory.dbQuery {
            StudentTable.update(
                {StudentTable.userId.eq(userId)}
            ){
                student->
                student[StudentTable.userId] = userId
                student[StudentTable.name] = name
                student[StudentTable.age] = age
            }
        }

    override suspend fun getAllStudents(): List<Student> =
        DatabaseFactory.dbQuery {
            StudentTable.selectAll().mapNotNull {
                //Filters out any null results that may
                //arise from the conversion process, using mapNotNull
                rowToData(it)
            }
        }

    override suspend fun getStudentById(userId: Int): Student? =
        DatabaseFactory.dbQuery {
            StudentTable.select {
                StudentTable.userId.eq(userId)
            }.map {
                rowToData(it)
            }.singleOrNull()
        }


    //Postgrss returns row not dataClass but we need to return data so we convert row to data

    private fun rowToData(row:ResultRow?):Student?{
        if (row==null)
            return null
        return Student(
            userId = row[StudentTable.userId],
            name =   row[StudentTable.name],
            age =    row[StudentTable.age]
        )
    }
}