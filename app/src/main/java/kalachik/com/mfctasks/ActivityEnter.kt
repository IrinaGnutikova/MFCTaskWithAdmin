package kalachik.com.mfctasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kalachik.com.mfctasks.admin.MainActivityAdmin
import kalachik.com.mfctasks.emploee.MainActivityEmp

class ActivityEnter : AppCompatActivity() { //вход
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        auth = Firebase.auth
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){ // если пользователь уже входил ранее, но не вышел
            Toast.makeText(baseContext, "С возвращением",
                Toast.LENGTH_SHORT).show()
            if (currentUser.email == "admin@gmail.com"){
                val intent = Intent(this, MainActivityAdmin::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, MainActivityEmp::class.java)
                startActivity(intent)
            }
        }

        val login = findViewById<Button>(R.id.btnEnter)
        login.setOnClickListener {
            val email = findViewById<EditText>(R.id.etLog)
            val pass = findViewById<EditText>(R.id.etPass)
            val email1 = email.text.toString()
            val password = pass.text.toString()

            if (email.text.isEmpty() || pass.text.isEmpty()){
                Toast.makeText(baseContext, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email1, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(baseContext, "Вы вошли успешно!",
                            Toast.LENGTH_SHORT).show()
                        if (email1 == "admin@gmail.com"){
                            val intent = Intent(this, MainActivityAdmin::class.java)
                            startActivity(intent)
                        }
                        else {
                            val intent = Intent(this, MainActivityEmp::class.java)
                            startActivity(intent)
                        }

                    }
                    else {

                        Toast.makeText(baseContext, "Ошибка входа. Попробуйте еще раз",
                            Toast.LENGTH_SHORT).show()

                    }
                }
                .addOnFailureListener{

                    Toast.makeText(baseContext, "Ошибка входа. ${it.localizedMessage}",
                        Toast.LENGTH_SHORT).show()

                }
        }
    }
}
