package kalachik.com.mfctasks.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kalachik.com.mfctasks.R

class ActivityAddEmp : AppCompatActivity() { //добавление сотрудника
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_emp)

        auth = Firebase.auth

        val register = findViewById<Button>(R.id.btnRegister)
        register.setOnClickListener {

            val email = findViewById<EditText>(R.id.email)
            val pass = findViewById<EditText>(R.id.password)
            val name = findViewById<EditText>(R.id.name)

            val email1 = email.text.toString()
            val password = pass.text.toString()
            var name1 = name.text.toString()

            if (email.text.isEmpty() || pass.text.isEmpty()) {
                Toast.makeText(baseContext, "Ошибка регистрации! Проверьте поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email1, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "Аккаунт успешно создан!",
                            Toast.LENGTH_SHORT
                        ).show()
                        auth = Firebase.auth
                        var user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name1)
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                            }

                        if (user != null) {
                            dbref = FirebaseDatabase.getInstance().reference.child("Users")
                                .child(user.uid)
                            if (name1 != null) {
                                dbref.child("name").setValue(name1)
                            }
                            else{
                                dbref.child("name").setValue("Имя не установлено")
                            }
                            dbref.child("email").setValue(user.email)
                            dbref.child("password").setValue(password)
                        }

                        auth.signOut()
                        auth.signInWithEmailAndPassword("admin@gmail.com","adminmfc")

                        val intent = Intent(this, MainActivityAdmin::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            baseContext, "Ошибка!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Ошибка: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}