package com.betweentwobits.proxytest

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var headerText: TextView
    private lateinit var bodyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        headerText = findViewById(R.id.header_text)
        bodyText = findViewById(R.id.body_text)

        requestFile()
    }

    private fun requestFile() {
        val request = Request.Builder()
            .url("http://date.jsontest.com/")
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

                    val headersBuilder = StringBuilder()
                    for ((name, value) in response.headers) {
                        headersBuilder.append("$name: $value")
                        headersBuilder.append("\n")
                    }

                    val headers = headersBuilder.toString()
                    Log.d("Response Headers", headers)
                    headerText.text = headers

                    val body = response.body?.string().orEmpty()
                    Log.d("Response Body", body)
                    bodyText.text = body
                }
            }
        })
    }
}
