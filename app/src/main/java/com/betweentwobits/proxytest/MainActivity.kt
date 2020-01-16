package com.betweentwobits.proxytest

import android.os.Bundle
import android.util.Log
import android.widget.Button
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
    private lateinit var responseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        responseText = findViewById(R.id.response_text)

        val refreshButton = findViewById<Button>(R.id.refresh_button)
        refreshButton.setOnClickListener {
            requestFile()
        }

        requestFile()
    }

    private fun requestFile() {
        val request = Request.Builder()
            .url("http://date.jsontest.com/")
            .build()

        // http://headers.jsontest.com/

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        val error = "Unexpected response: \nCode: ${response.code}\nMessage: ${response.message}"
                        responseText.text = error
                        throw IOException(error)
                    }

                    val responseBuilder = StringBuilder()
                    for ((name, value) in response.headers) {
                        responseBuilder.append("$name: $value")
                        responseBuilder.append("\n")
                    }

                    val body = response.body?.string().orEmpty()
                    responseBuilder.append(body)

                    val responseString = responseBuilder.toString()
                    Log.d("MainActivity", "Response: $responseString")
                    responseText.text = responseString
                }
            }
        })
    }
}
