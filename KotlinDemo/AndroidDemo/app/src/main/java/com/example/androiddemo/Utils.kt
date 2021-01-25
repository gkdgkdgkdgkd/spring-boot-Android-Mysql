package com.example.kotlindemo

import android.content.Context
import android.os.Message
import android.widget.Toast
import org.bouncycastle.jcajce.provider.digest.Keccak
import org.bouncycastle.util.encoders.Hex

class Utils {
    companion object{
        private val digest512 = Keccak.Digest512();
        fun encrypt(origin:String) = String(Hex.encode(digest512.digest(origin.toByteArray(Charsets.UTF_8))))

        fun getResponseMessage(code:Int):String{
            var message = ""
            when(code){
                ResponseCode.SIGN_IN_SUCCESS -> message = "登录成功"
                ResponseCode.SIGN_UP_SUCCESS -> message = "注册成功"
                ResponseCode.SIGN_IN_FAILED -> message = "用户名或密码错误"
                ResponseCode.SIGN_UP_FAILED -> message = "注册失败"
                ResponseCode.DELETE_FAILED -> message = "删除失败"
                ResponseCode.DELETE_SUCCESS -> message = "删除成功,自动退出"
                ResponseCode.UPDATE_SUCCESS -> message = "更新成功"
                ResponseCode.UPDATE_FAILED -> message = "更新失败"
                ResponseCode.EMPTY_RESPONSE -> message = "响应体为空"
                ResponseCode.SERVER_ERROR -> message = "服务器错误"
                ResponseCode.JSON_SERIALIZATION -> message = "JSON序列化错误"
                ResponseCode.EXIT_SUCCESS -> message = "退出成功"
                ResponseCode.REQUEST_FAILED -> message = "请求发送失败"
                ResponseCode.UNCHANGED_INFORMATION -> message = "未修改信息"
            }
            return message
        }

        fun showMessage(context:Context,message:Message) =
            Toast.makeText(context, getResponseMessage(message.what),Toast.LENGTH_SHORT).show()
    }
}