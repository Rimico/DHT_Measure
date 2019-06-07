package com.dhtm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import java.security.KeyStore

class MainActivity : AppCompatActivity() {

    private lateinit var layout : ConstraintLayout
    private lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.ac_ma)
        btn = findViewById(R.id.At_Nat)
        btn.setOnClickListener{
            val f = btn.text == "자동"
            btn.text = if (f) "수동" else "자동"
            layout.isClickable = f
        }
        layout.setOnClickListener{
            Toast.makeText(applicationContext, "수치를 다시 기재합니다.", Toast.LENGTH_LONG).show()
        }

    }
}

