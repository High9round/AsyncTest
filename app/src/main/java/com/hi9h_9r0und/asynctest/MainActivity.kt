package com.hi9h_9r0und.asynctest

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    val Tag: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallBtn.setOnClickListener{
            AsyncTaskExample().execute()
        }
    }

    inner class AsyncTaskExample: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            MyprogressBar.visibility = View.VISIBLE;
        }

        override fun doInBackground(vararg p0: String?): String {

            var Result: String = ""
            //It will return current data and time.
            val API_URL = "http://androidpala.com/tutorial/http.php?get=1"

            try {

                val URL = URL(API_URL)
                val connect = URL.openConnection() as HttpURLConnection

                connect.readTimeout = 8000
                connect.connectTimeout = 8000
                connect.requestMethod = "GET"
                connect.doOutput = true
                connect.connect()

                val ResponseCode: Int = connect.responseCode;
                Log.d(Tag, "ResponseCode" + ResponseCode)

                if (ResponseCode == 200) {
                    val tempStream: InputStream = connect.inputStream;
                    if (tempStream != null) {
                        Result = ConvertToString(tempStream)
                    }
                }
            } catch(Ex: Exception) {
                Log.d("", "Error in doInBackground " + Ex.message)
            }
            return Result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            MyprogressBar.visibility = View.INVISIBLE;

            if (result == "") {
                my_text.text = "Network Error"
            } else {
                my_text.text = result
            }
        }
    }

    fun ConvertToString(inStream: InputStream): String {

        var Result: String = ""
        val isReader = InputStreamReader(inStream)
        var bReader = BufferedReader(isReader)
        var temp_str: String?

        try {

            while (true) {
                temp_str = bReader.readLine()
                if (temp_str == null) { break }
                Result += temp_str
            }
        } catch(Ex: Exception) {
            Log.e(Tag, "Error in ConvertToString " + Ex.printStackTrace())
        }
        return Result
    }
}
