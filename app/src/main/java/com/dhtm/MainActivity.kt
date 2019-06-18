package com.dhtm

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket

class MainActivity : FragmentActivity()  {

    private lateinit var layout : ConstraintLayout
    private lateinit var btn : Button
    private lateinit var temperature_display : TextView
    private lateinit var humidity_display : TextView
    private lateinit var dust_display : TextView
    private lateinit var micro_dust_display : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.At_Nat)
        layout = findViewById(R.id.ac_ma)
        layout.isClickable = false

        btn.setOnClickListener {
            val f = btn.text == "자동"
            btn.text = if (f) "수동" else "자동"
            layout.isClickable = f
        }
        layout.setOnClickListener {
            Toast.makeText(applicationContext, "수치를 다시 기재합니다.", Toast.LENGTH_LONG).show()
        }

        temperature_display = findViewById(R.id.temperature_display)
        humidity_display = findViewById(R.id.humidity_display)
        dust_display = findViewById(R.id.dust_display)
        micro_dust_display = findViewById(R.id.micro_dust_display)

        object : AsyncTask<Void, Data, Void>() {
            var m_cancel = false
            lateinit var inputAsString: BufferedReader
            lateinit var outputAsString: BufferedWriter

            @RequiresApi(Build.VERSION_CODES.O)
            override fun doInBackground(vararg p0: Void?): Void? {
                val socket = Socket("192.168.0.16", 333)
                inputAsString = socket.getInputStream().bufferedReader()
                outputAsString = socket.getOutputStream().bufferedWriter()
                outputAsString.write("auto")
                outputAsString.flush()

                while (true) {
                    val input = inputAsString.readLine()
                    if (input != null) {
                        val n : UInt = input.toUInt(16)
                        publishProgress(
                            Data(
                                (n shr 24).toByte(),
                                ((n and 0x00FF0000u) shr 16).toByte(),
                                ((n and 0x0000FF00u) shr 8).toByte(),
                                (n and 0x000000FFu).toByte()
                            )
                        )
                    }
                    if (m_cancel) break
                }
                return null
            }

            override fun onProgressUpdate(vararg values: MainActivity.Data?) {
                var item = values[0]
                if (item != null) {
                    humidity_display.text = item.h.toString()
                    temperature_display.text = item.t.toString()
                    dust_display.text = item.pm25.toString()
                    micro_dust_display.text = item.pm10.toString()
                }
            }

            override fun onCancelled() {
                super.onCancelled()
                m_cancel = true
            }


        }.execute()


    }
    data class Data(val h: Byte, val t: Byte, val pm10: Byte, val pm25: Byte)
}
