package com.example.kotlindemo

data class User(var id:Int=0,var name:String="",var password:String=""){
    constructor(name: String,password: String) : this() {
        this.name = name
        this.password = password
    }
}
