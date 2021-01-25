package com.example.kotlindemo

class NetworkSettings {
    companion object{
        private const val HOST = "192.168.1.8"
        private const val PORT = "8080"
        private const val PROTOCOL = "http"
        const val SIGN_IN_UP = "$PROTOCOL://$HOST:$PORT/sign/in/up"
        const val UPDATE = "$PROTOCOL://$HOST:$PORT/update"
        const val DELETE = "$PROTOCOL://$HOST:$PORT/delete"
    }
}