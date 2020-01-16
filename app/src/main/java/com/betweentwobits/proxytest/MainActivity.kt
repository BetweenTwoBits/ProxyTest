package com.betweentwobits.proxytest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestFile()
    }

    private fun requestFile() {
        val request = Request.Builder()
            .url("https://www.test.com/list.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected response code: $response")
                    }

                    for ((name, value) in response.headers) {
                        Log.d("Response Headers", "$name: $value")
                    }

                    Log.d("Response Body", response.body?.string().orEmpty())
                }

            }
        })
    }
}
