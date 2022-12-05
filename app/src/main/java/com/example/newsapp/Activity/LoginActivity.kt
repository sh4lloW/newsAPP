package com.example.newsapp.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.newsapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login = findViewById<Button>(R.id.login)
        val register = findViewById<Button>(R.id.register)
        val accountEdit = findViewById<EditText>(R.id.accountEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)

        val prefs = getPreferences(Context.MODE_PRIVATE)
        val editor = prefs.edit()

        login.setOnClickListener {
            //输入的账号密码
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()

            if (password == prefs.getString(account,"") && account.length > 1) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "账号或密码错误", Toast.LENGTH_SHORT).show()
            }
        }

        register.setOnClickListener{
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            editor.putString(account,password)
            editor.apply()
            Toast.makeText(this@LoginActivity, "注册成功", Toast.LENGTH_SHORT).show()
        }
    }

}
