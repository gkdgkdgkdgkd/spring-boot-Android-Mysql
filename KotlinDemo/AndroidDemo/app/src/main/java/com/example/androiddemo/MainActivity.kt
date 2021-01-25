package com.example.androiddemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import com.example.androiddemo.databinding.ActivityMainBinding
import com.example.kotlindemo.*
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    private val mapper = ObjectMapper()
    private var signInId = 0
    private val handler = Handler(Looper.getMainLooper())
    private val mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
    private val message = Message()
    private var oldName = ""
    private var oldPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun signInUp(view: View) {
        try {
            val name = binding.name.text.toString()
            val password = Utils.encrypt(binding.password.text.toString())
            val request = Request.Builder().url(NetworkSettings.SIGN_IN_UP).post(
                    mapper.writeValueAsString(User(name, password)).toRequestBody(mediaType)
            ).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    message.what = ResponseCode.REQUEST_FAILED
                    handler.post {
                        Utils.showMessage(applicationContext, message)
                    }
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body != null) {
                            val restResponse = mapper.readValue(body.string(), RestResponse::class.java)
                            message.what = restResponse.code
                            if (message.what == ResponseCode.SIGN_IN_SUCCESS) {
                                handler.post {
                                    signInId = restResponse.data as? Int ?: 0
                                    binding.update.visibility = View.VISIBLE
                                    binding.delete.visibility = View.VISIBLE
                                    binding.signInUp.text = "退出"
                                    binding.signInUp.setOnClickListener { signOut(false) }
                                    oldName = binding.name.text.toString()
                                    oldPassword = binding.password.text.toString()
                                }
                            }
                        } else {
                            message.what = ResponseCode.EMPTY_RESPONSE
                            Log.e("RESPONSE_BODY_EMPTY", response.message)
                        }
                    } else {
                        message.what = ResponseCode.SERVER_ERROR
                        Log.e("SERVER_ERROR", response.message)
                    }
                    handler.post {
                        Utils.showMessage(applicationContext, message)
                    }
                }
            })
        } catch (e: Exception) {
            message.what = ResponseCode.JSON_SERIALIZATION
            Utils.showMessage(applicationContext, message)
            e.printStackTrace()
        }
    }

    fun update(view: View) {
        try {
            val name = binding.name.text.toString()
            var password = binding.password.text.toString()
            if (name == oldName && password == oldPassword) {
                message.what = ResponseCode.UNCHANGED_INFORMATION
                handler.post {
                    Utils.showMessage(applicationContext, message)
                }
                return
            }
            password = Utils.encrypt(password)
            val request = Request.Builder().url(NetworkSettings.UPDATE).put(
                    mapper.writeValueAsString(User(signInId, name, password)).toRequestBody(mediaType)
            ).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    message.what = ResponseCode.REQUEST_FAILED
                    handler.post {
                        Utils.showMessage(applicationContext, message)
                    }
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body != null) {
                            val restResponse = mapper.readValue(body.string(), RestResponse::class.java)
                            message.what = restResponse.code
                            if (message.what == ResponseCode.UPDATE_SUCCESS) {
                                oldName = binding.name.text.toString()
                                oldPassword = binding.password.text.toString()
                            }
                        } else {
                            message.what = ResponseCode.EMPTY_RESPONSE
                            Log.e("RESPONSE_BODY_EMPTY", response.message)
                        }
                    } else {
                        message.what = ResponseCode.SERVER_ERROR
                        Log.e("SERVER_ERROR", response.message)
                    }
                    handler.post {
                        Utils.showMessage(applicationContext, message)
                    }
                }
            })
        } catch (e: Exception) {
            message.what = ResponseCode.JSON_SERIALIZATION
            Utils.showMessage(applicationContext, message)
            e.printStackTrace()
        }
    }

    fun delete(view: View) {
        try {
            val request = Request.Builder().url("${NetworkSettings.DELETE}?id=$signInId").delete().build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    message.what = ResponseCode.REQUEST_FAILED
                    handler.post {
                        Utils.showMessage(applicationContext, message)
                    }
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body != null) {
                            val restResponse = mapper.readValue(body.string(), RestResponse::class.java)
                            message.what = restResponse.code
                            if (message.what == ResponseCode.DELETE_SUCCESS) {
                                handler.post {
                                    signOut(true)
                                }
                            }
                        } else {
                            message.what = ResponseCode.EMPTY_RESPONSE
                            Log.e("RESPONSE_BODY_EMPTY", response.message)
                        }
                    } else {
                        message.what = ResponseCode.SERVER_ERROR
                        Log.e("SERVER_ERROR", response.message)
                    }
                    handler.post {
                        Utils.showMessage(applicationContext, message)
                    }
                }
            })
        } catch (e: Exception) {
            message.what = ResponseCode.JSON_SERIALIZATION
            Utils.showMessage(applicationContext, message)
            e.printStackTrace()
        }
    }

    fun signOut(isDeleted: Boolean) {
        signInId = 0
        binding.signInUp.text = "注册/登录"
        binding.signInUp.setOnClickListener { v -> signInUp(v) }
        binding.name.setText("")
        binding.password.setText("")
        binding.update.visibility = View.INVISIBLE
        binding.delete.visibility = View.INVISIBLE
        if (!isDeleted) {
            message.what = ResponseCode.EXIT_SUCCESS
        }
        Utils.showMessage(applicationContext, message)
    }
}