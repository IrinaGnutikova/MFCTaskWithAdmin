package kalachik.com.mfctasks.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kalachik.com.mfctasks.ActivityEnter
import kalachik.com.mfctasks.R

class MainActivityAdmin : AppCompatActivity() { //главное окно администратора
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)

        val seeEmp = findViewById<Button>(R.id.btnSeeEmp)
        seeEmp.setOnClickListener {
            val intent = Intent(this, ActivitySeeEmp::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth

        val logout = findViewById<Button>(R.id.btnExit)
        logout.setOnClickListener {
            auth.signOut()
            Toast.makeText(
                baseContext, "Вы вышли",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, ActivityEnter::class.java)
            startActivity(intent)

        }

        val addEmp = findViewById<Button>(R.id.btnAddEmp)
        addEmp.setOnClickListener {

            val intent = Intent(this, ActivityAddEmp::class.java)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        Toast.makeText(baseContext, "Чтобы выйти из аккаунта нажмите на кнопку внизу", Toast.LENGTH_LONG).show()
    }
}