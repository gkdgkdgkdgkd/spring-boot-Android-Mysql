package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.response.ResponseBody;
import com.example.demo.response.ResponseCode;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService service;

    @PostMapping("sign/in/up")
    public ResponseBody signInUp(@RequestBody User user) {
        if (service.exists(user)) {
            User u = service.findByNameAndPassword(user);
            return new ResponseBody(u != null ? ResponseCode.SIGN_IN_SUCCESS : ResponseCode.SIGN_IN_FAILED, u != null ? u.getId() : "");
        }
        return new ResponseBody(service.insert(user) ? ResponseCode.SIGN_UP_SUCCESS : ResponseCode.SIGN_UP_FAILED, "");
    }

    @PutMapping("update")
    public ResponseBody update(@RequestBody User user) {
        return new ResponseBody(service.update(user) ? ResponseCode.UPDATE_SUCCESS : ResponseCode.UPDATE_FAILED, "");
    }

    @DeleteMapping("delete")
    public ResponseBody deleteByName(@RequestParam int id) {
        return new ResponseBody(service.deleteById(id) ? ResponseCode.DELETE_SUCCESS : ResponseCode.DELETE_FAILED, "");
    }

    @GetMapping("test")
    public String test() {
        return "test";
    }
}
