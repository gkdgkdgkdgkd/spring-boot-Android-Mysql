package com.example.demo.service

import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class UserService {
    @Autowired
    lateinit var repository:UserRepository

    fun exists(user: User) = repository.existsByName(user.name)

    fun findByNameAndPassword(user:User) = repository.findByNameAndPassword(user.name,user.password)

    fun insert(user:User):Boolean{
        repository.save(user)
        return true
    }

    fun update(user:User):Boolean{
        return if(repository.findById(user.id).isEmpty){
            false
        }else{
            repository.save(user)
            true
        }
    }

    fun deleteById(id:Int):Boolean{
        return if(!repository.existsById(id)) {
            false
        }else{
            repository.deleteById(id)
            true
        }
    }
}