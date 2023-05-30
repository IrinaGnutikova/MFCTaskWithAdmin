package kalachik.com.mfctasks.admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kalachik.com.mfctasks.R
import kalachik.com.mfctasks.admin.ActivitySeeEmp.userHelp.emailEmp
import kalachik.com.mfctasks.admin.ActivitySeeEmp.userHelp.passEmp
import kalachik.com.mfctasks.listEmp.AdapterEmp
import kalachik.com.mfctasks.listEmp.Emploee

class ActivitySeeEmp : AppCompatActivity() { //отображение сотрудников
    private lateinit var dbref: DatabaseReference
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var empArrayList: ArrayList<Emploee>
    private lateinit var auth: FirebaseAuth

    object userHelp{
        var emailEmp = ""
        var passEmp = ""
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_emp)

        empRecyclerView = findViewById(R.id.emp_recyclerView)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        empArrayList = arrayListOf<Emploee>()
        empArrayList.clear()
        getEmpData()



        val back = findViewById<ImageButton>(R.id.btnExit)
        back.setOnClickListener{
            auth.signOut()
            auth.signInWithEmailAndPassword("admin@gmail.com", "adminmfc")
            val intent = Intent(this, MainActivityAdmin::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth

        val goSee = findViewById<ImageButton>(R.id.imbtnGo) //переход для просмотра записей и действий с сотрудником
        goSee.setOnClickListener{
            val emailGo = findViewById<EditText>(R.id.email)
            val pasGo = findViewById<EditText>(R.id.pas)
            val email = emailGo.text.toString()
            val password = pasGo.text.toString()

            if (emailGo.text.isEmpty() || pasGo.text.isEmpty()){
                Toast.makeText(baseContext, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        emailEmp = email
                        passEmp = password
                        val intent = Intent(this@ActivitySeeEmp, SeeTaskEmp::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@ActivitySeeEmp, "Неправильные данные о сотруднике!", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(baseContext, "Ошибка входа. ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun getEmpData() { //получение данных о сотрудниках в recycler

            dbref = FirebaseDatabase.getInstance().getReference("Users")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (taskSnapshot in snapshot.children) {
                            val emp = taskSnapshot.getValue(Emploee::class.java)
                            empArrayList.add(emp!!)
                        }
                        empRecyclerView.adapter = AdapterEmp(empArrayList)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onBackPressed() {
        Toast.makeText(baseContext, "Чтобы вернуться нажмите на стрелочку наверху", Toast.LENGTH_LONG).show()
    }
}