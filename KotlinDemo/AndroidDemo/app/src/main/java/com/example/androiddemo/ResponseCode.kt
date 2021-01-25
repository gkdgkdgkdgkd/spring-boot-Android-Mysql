package com.example.kotlindemo

class ResponseCode {
    companion object{
        const val SIGN_IN_SUCCESS = 2000
        const val SIGN_UP_SUCCESS = 2001
        const val UPDATE_SUCCESS = 2002
        const val DELETE_SUCCESS = 2003

        const val SIGN_IN_FAILED = 3000
        const val SIGN_UP_FAILED = 3001
        const val UPDATE_FAILED = 3002
        const val DELETE_FAILED = 3003

        const val EMPTY_RESPONSE = 4000
        const val SERVER_ERROR = 4001
        const val REQUEST_FAILED = 4002
        const val JSON_SERIALIZATION = 4003
        const val EXIT_SUCCESS = 4004
        const val UNCHANGED_INFORMATION = 4005
    }
}