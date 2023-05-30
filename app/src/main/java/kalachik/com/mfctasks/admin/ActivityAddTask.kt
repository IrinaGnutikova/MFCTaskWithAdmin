package kalachik.com.mfctasks.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kalachik.com.mfctasks.R
import kalachik.com.mfctasks.admin.ActivitySeeEmp.userHelp.emailEmp
import kalachik.com.mfctasks.admin.ActivitySeeEmp.userHelp.passEmp

class ActivityAddTask : AppCompatActivity() { //добавление записей для сотрудника
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_add_task)

            val saveText = findViewById<Button>(R.id.btnSave)
            saveText.setOnClickListener {

                auth = Firebase.auth

                val email = emailEmp
                val password = passEmp
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val db = FirebaseFirestore.getInstance()
                            if (user != null) {
                                db.collection("users").document(user.uid)
                                    .get()
                                val userid = user.uid
                                dbref = FirebaseDatabase.getInstance().reference.child(userid).push().child("taskName")
                                val newtask = findViewById<EditText>(R.id.textTask)
                                val task = newtask.text.toString()

                                dbref.setValue(task).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        Toast.makeText(baseContext, "Запись добавлена!", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, SeeTaskEmp::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener{
                        Toast.makeText(baseContext, "Ошибка добавления. ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }

            }
        }

    override fun onStop() {
        super.onStop()
        auth.signOut()
        auth.signInWithEmailAndPassword("admin@gmail.com", "adminmfc")
    }

}