package org.startup.project.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_menu)

        val mapActbutton = findViewById<Button>(R.id.map)
        mapActbutton.setOnClickListener{
            val Intent = Intent(this,MainActivity::class.java)
            startActivity(Intent)
        }

        val logoutbutton = findViewById<Button>(R.id.logout)
        logoutbutton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val Intent = Intent(this,logActivity::class.java)
            startActivity(Intent)
        }
    }

}