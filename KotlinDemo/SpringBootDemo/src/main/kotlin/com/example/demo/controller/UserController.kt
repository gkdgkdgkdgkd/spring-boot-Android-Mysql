package com.example.demo.controller

import com.example.demo.entity.User
import com.example.demo.response.ResponseBody
import com.example.demo.response.ResponseCode
import com.example.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class UserController {
    @Autowired
    lateinit var service:UserService

    @PostMapping("sign/in/up")
    fun signInUp(@RequestBody user: User):ResponseBody{
        if(service.exists(user)){
            val u = service.findByNameAndPassword(user)
            return ResponseBody(
                ResponseCode.SIGN_IN_SUCCESS.takeIf { u!=null } ?: ResponseCode.SIGN_IN_FAILED,
                u?.id ?: ""
            )
        }
        return ResponseBody(ResponseCode.SIGN_UP_SUCCESS.takeIf { service.insert(user) } ?: ResponseCode.SIGN_UP_FAILED)
    }

    @PutMapping("update")
    fun update(@RequestBody user:User) =
        ResponseBody(ResponseCode.UPDATE_SUCCESS.takeIf { service.update(user) } ?: ResponseCode.UPDATE_FAILED)

    @DeleteMapping("delete")
    fun delete(@RequestParam id:Int) =
        ResponseBody(ResponseCode.DELETE_SUCCESS.takeIf { service.deleteById(id) } ?: ResponseCode.DELETE_FAILED)
}