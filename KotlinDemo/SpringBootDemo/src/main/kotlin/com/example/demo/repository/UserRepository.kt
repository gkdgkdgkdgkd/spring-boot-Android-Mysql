package com.example.demo.repository

import com.example.demo.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository:CrudRepository<User,Int> {
    fun existsByName(name:String):Boolean
    fun findByNameAndPassword(name:String,password:String):User?
}